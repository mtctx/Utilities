@file:Suppress("unused")

package dev.mtctx.utilities

import dev.mtctx.utilities.runCatchingOutcomeOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import dev.mtctx.utilities.serialization.jsonForMachines
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import java.io.File

/**
 * The default [okio.FileSystem] instance for file operations, using the system's native file system.
 */
val fileSystem = FileSystem.SYSTEM

/**
 * Serializes this object to a string using the specified [serializer] and [format].
 *
 * @param serializer The [kotlinx.serialization.KSerializer] to use for serialization.
 * @param format The [kotlinx.serialization.StringFormat] to use for encoding (default: [dev.mtctx.utilities.serialization.jsonForMachines]).
 * @return The serialized string representation of this object.
 * @see String.deserialize
 */
fun <T> T.serialize(serializer: KSerializer<T>, format: StringFormat = jsonForMachines): String =
    format.encodeToString(serializer, this)

/**
 * Deserializes this string to an object of type [T] using the specified [deserializer] and [format].
 *
 * @param deserializer The [kotlinx.serialization.KSerializer] to use for deserialization.
 * @param format The [kotlinx.serialization.StringFormat] to use for decoding (default: [dev.mtctx.utilities.serialization.jsonForMachines]).
 * @return The deserialized object of type [T].
 * @throws kotlinx.serialization.SerializationException if deserialization fails.
 * @see serialize
 */
fun <T> String.deserialize(deserializer: KSerializer<T>, format: StringFormat = jsonForMachines): T =
    format.decodeFromString(deserializer, this)

/**
 * Serializes this object and writes it to the specified [path].
 *
 * Creates parent directories if needed and supports appending or overwriting the file.
 * Optionally uses an atomic move to ensure safe file writing.
 *
 * @param serializer The [kotlinx.serialization.KSerializer] to use for serialization.
 * @param path The [okio.Path] to write the serialized data to.
 * @param append If `true`, appends to the file; otherwise, overwrites it (default: `true`).
 * @param atomicMove If `true`, writes to a temporary file and moves it atomically to [path] (default: `false`).
 * @param format The [kotlinx.serialization.StringFormat] to use for encoding (default: [dev.mtctx.utilities.serialization.jsonForMachines]).
 * @return This object, for method chaining.
 */
suspend fun <T : Any> T.serializeAndWrite(
    serializer: KSerializer<T>,
    path: Path,
    append: Boolean = true,
    atomicMove: Boolean = false,
    format: StringFormat = jsonForMachines
): Outcome<T> = runCatchingOutcomeOf {
    path.parent?.let { fileSystem.createDirectories(it) }
    val writablePath = if (atomicMove) path.sibling("${path.name}.temp") else path

    if (append)
        fileSystem.appendingSink(writablePath).buffer().use { it.writeUtf8(serialize(serializer, format)) }
    else fileSystem.write(writablePath) { writeUtf8(serialize(serializer, format)) }

    if (atomicMove && writablePath != path)
        fileSystem.atomicMove(writablePath, path)

    return@runCatchingOutcomeOf this
}

/**
 * Reads the content of the specified [okio.Path] and deserializes it to an object of type [T].
 *
 * @param deserializer The [kotlinx.serialization.KSerializer] to use for deserialization.
 * @param format The [kotlinx.serialization.StringFormat] to use for decoding (default: [dev.mtctx.utilities.serialization.jsonForMachines]).
 * @return The deserialized object of type [T].
 * @throws okio.IOException if reading the file fails.
 * @throws kotlinx.serialization.SerializationException if deserialization fails.
 */
suspend fun <T : Any> Path.readAndDeserialize(
    deserializer: KSerializer<T>,
    format: StringFormat = jsonForMachines
): Outcome<T> = runCatchingOutcomeOf<T> {
    fileSystem.read(this) { readUtf8() }.deserialize(deserializer, format)
}

/**
 * Reads the content of the specified [File] and deserializes it to an object of type [T].
 *
 * @param deserializer The [kotlinx.serialization.KSerializer] to use for deserialization.
 * @param format The [kotlinx.serialization.StringFormat] to use for decoding (default: [dev.mtctx.utilities.serialization.jsonForMachines]).
 * @return The deserialized object of type [T].
 * @throws java.io.IOException if reading the file fails.
 * @throws kotlinx.serialization.SerializationException if deserialization fails.
 */
suspend fun <T : Any> File.readAndDeserialize(
    deserializer: KSerializer<T>,
    format: StringFormat = jsonForMachines
): Outcome<T> = runCatchingOutcomeOf<T> {
    inputStream().bufferedReader().readText().deserialize(deserializer, format)
}

/**
 * Creates a [okio.Path] for a sibling file or directory with the specified [name].
 *
 * If this [okio.Path] has a parent, the sibling path is created under the same parent.
 * Otherwise, the [name] is converted directly to a [okio.Path].
 *
 * @param name The name of the sibling file or directory.
 * @return A [okio.Path] representing the sibling.
 */
fun Path.sibling(name: String): Path =
    parent?.div(name) ?: name.toPath()