/*
 * Utilities (Utilities.main): IO.kt
 * Copyright (C) 2025 mtctx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the **GNU General Public License** as published
 * by the Free Software Foundation, either **version 3** of the License, or
 * (at your option) any later version.
 *
 * This program is distributed WITHOUT ANY WARRANTY; see the
 * GNU General Public License for more details, which you should have
 * received with this program.
 *
 * SPDX-FileCopyrightText: 2025 mtctx
 * SPDX-License-Identifier: GPL-3.0-only
 */

package mtctx.utilities

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import mtctx.utilities.serialization.jsonForMachines
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import java.io.File

/**
 * The default [FileSystem] instance for file operations, using the system's native file system.
 */
val fileSystem = FileSystem.SYSTEM

/**
 * Serializes this object to a string using the specified [serializer] and [format].
 *
 * @param serializer The [KSerializer] to use for serialization.
 * @param format The [StringFormat] to use for encoding (default: [jsonForMachines]).
 * @return The serialized string representation of this object.
 * @see String.deserialize
 */
fun <T> T.serialize(serializer: KSerializer<T>, format: StringFormat = jsonForMachines): String =
    format.encodeToString(serializer, this)

/**
 * Deserializes this string to an object of type [T] using the specified [deserializer] and [format].
 *
 * @param deserializer The [KSerializer] to use for deserialization.
 * @param format The [StringFormat] to use for decoding (default: [jsonForMachines]).
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
 * @param serializer The [KSerializer] to use for serialization.
 * @param path The [Path] to write the serialized data to.
 * @param append If `true`, appends to the file; otherwise, overwrites it (default: `true`).
 * @param atomicMove If `true`, writes to a temporary file and moves it atomically to [path] (default: `false`).
 * @param format The [StringFormat] to use for encoding (default: [jsonForMachines]).
 * @return This object, for method chaining.
 */
fun <T> T.serializeAndWrite(
    serializer: KSerializer<T>,
    path: Path,
    append: Boolean = true,
    atomicMove: Boolean = false,
    format: StringFormat = jsonForMachines
): T {
    path.parent?.let { fileSystem.createDirectories(it) }
    val writablePath = if (atomicMove) path.sibling("${path.name}.temp") else path

    if (append)
        fileSystem.appendingSink(writablePath).buffer().use { it.writeUtf8(serialize(serializer, format)) }
    else fileSystem.write(writablePath) { writeUtf8(serialize(serializer, format)) }

    if (atomicMove && writablePath != path)
        fileSystem.atomicMove(writablePath, path)

    return this
}

/**
 * Reads the content of the specified [Path] and deserializes it to an object of type [T].
 *
 * @param deserializer The [KSerializer] to use for deserialization.
 * @param format The [StringFormat] to use for decoding (default: [jsonForMachines]).
 * @return The deserialized object of type [T].
 * @throws okio.IOException if reading the file fails.
 * @throws kotlinx.serialization.SerializationException if deserialization fails.
 */
fun <T> Path.readAndDeserialize(deserializer: KSerializer<T>, format: StringFormat = jsonForMachines): T =
    fileSystem.read(this) { readUtf8() }.deserialize(deserializer, format)

/**
 * Reads the content of the specified [File] and deserializes it to an object of type [T].
 *
 * @param deserializer The [KSerializer] to use for deserialization.
 * @param format The [StringFormat] to use for decoding (default: [jsonForMachines]).
 * @return The deserialized object of type [T].
 * @throws java.io.IOException if reading the file fails.
 * @throws kotlinx.serialization.SerializationException if deserialization fails.
 */
fun <T> File.readAndDeserialize(deserializer: KSerializer<T>, format: StringFormat = jsonForMachines): T =
    inputStream().bufferedReader().readText().deserialize(deserializer, format)

/**
 * Creates a [Path] for a sibling file or directory with the specified [name].
 *
 * If this [Path] has a parent, the sibling path is created under the same parent.
 * Otherwise, the [name] is converted directly to a [Path].
 *
 * @param name The name of the sibling file or directory.
 * @return A [Path] representing the sibling.
 */
fun Path.sibling(name: String): Path =
    parent?.div(name) ?: name.toPath()