@file:Suppress("unused")
@file:OptIn(ExperimentalSerializationApi::class)

package dev.mtctx.utilities.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModuleBuilder

/**
 * Default [JsonFileFormat] instance for JSON serialization.
 */
val jsonFileFormat = JsonFileFormat()

/**
 * Default [kotlinx.serialization.json.Json] instance configured for human-readable output, using [jsonFileFormat].
 */
val jsonForHumans = jsonFileFormat.defaultForHumans

/**
 * Default [kotlinx.serialization.json.Json] instance configured for machine-readable output, using [jsonFileFormat].
 */
val jsonForMachines = jsonFileFormat.defaultForMachines

/**
 * Creates a [kotlinx.serialization.StringFormat] instance based on the specified [fileFormat] and configuration.
 *
 * @param forHumans If `true`, configures the format for human-readable output; otherwise, for machine-readable output.
 * @param fileFormat The [FileFormat] instance to use for creating the [kotlinx.serialization.StringFormat].
 * @param serializersModuleBuilder An optional builder to add custom serializers.
 * @return A [T] instance configured according to the specified parameters.
 * @see FileFormat
 * @see JsonFileFormat
 */
fun <T : StringFormat> stringFormat(
    forHumans: Boolean = false,
    fileFormat: FileFormat<T>,
    serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)? = null
): T {
    val formatFunction = if (forHumans) fileFormat::forHumans else fileFormat::forMachines
    return formatFunction(serializersModuleBuilder)
}

/**
 * Creates a [kotlinx.serialization.json.Json] instance based on the [jsonFileFormat].
 *
 * @param forHumans If `true`, configures the JSON format for human-readable output; otherwise, for machine-readable output.
 * @param serializersModuleBuilder An optional builder to add custom serializers.
 * @return A [kotlinx.serialization.json.Json] instance configured according to the specified parameters.
 * @see JsonFileFormat
 * @see stringFormat
 */
fun json(forHumans: Boolean = false, serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)? = null): Json =
    stringFormat(forHumans, jsonFileFormat, serializersModuleBuilder)


/**
 * Wraps a [kotlinx.serialization.modules.SerializersModuleBuilder] lambda for use in serialization configurations.
 *
 * This function is a utility to pass a [kotlinx.serialization.modules.SerializersModuleBuilder] lambda to classes like [dev.mtctx.utilities.serialization.FileFormat],
 * ensuring the lambda can be stored and applied to configure serializers.
 *
 * @param serializersModuleBuilder The lambda to configure a [kotlinx.serialization.modules.SerializersModuleBuilder].
 * @return The same lambda, typed for use in serialization configurations.
 * @see dev.mtctx.utilities.serialization.FileFormat
 */
fun serializersModuleBuilder(serializersModuleBuilder: SerializersModuleBuilder.() -> Unit): SerializersModuleBuilder.() -> Unit =
    serializersModuleBuilder