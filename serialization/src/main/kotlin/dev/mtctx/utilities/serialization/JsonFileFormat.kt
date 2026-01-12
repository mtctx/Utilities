package dev.mtctx.utilities.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder

/**
 * A [FileFormat] implementation for JSON serialization, providing configurations for human-readable
 * and machine-readable output.
 *
 * This class extends [FileFormat] to create [kotlinx.serialization.json.Json] instances with specific settings tailored for
 * human or machine use. The [forHumans] configuration includes features like pretty printing and
 * lenient parsing, while [forMachines] prioritizes compactness and strictness for efficient processing.
 * Both configurations support polymorphic serialization and custom serializers via [serializersModuleBuilders].
 *
 * @param serializersModuleBuilders A mutable set of builders to configure custom serializers (default: empty set).
 * @see FileFormat
 * @see kotlinx.serialization.json.Json
 */
@OptIn(ExperimentalSerializationApi::class)
class JsonFileFormat(serializersModuleBuilders: MutableSet<SerializersModuleBuilder.() -> Unit> = mutableSetOf()) :
    FileFormat<Json>(serializersModuleBuilders) {

    /**
     * Creates a [kotlinx.serialization.json.Json] instance configured for human-readable output.
     *
     * This configuration enables features like pretty printing, lenient parsing, trailing commas,
     * comments, and case-insensitive enum decoding. It is suitable for scenarios where the output
     * will be read or edited by humans.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers.
     * @return A [kotlinx.serialization.json.Json] instance configured for human-readable output.
     */
    override fun forHumans(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?) = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
        explicitNulls = true
        prettyPrintIndent = "  "
        coerceInputValues = true
        useArrayPolymorphism = false
        classDiscriminator = "type"
        allowSpecialFloatingPointValues = false
        decodeEnumsCaseInsensitive = true
        allowTrailingComma = true
        allowComments = true
        classDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC
        serializersModule = serializersModule(serializersModuleBuilder)
    }

    /**
     * Creates a [kotlinx.serialization.json.Json] instance configured for machine-readable output.
     *
     * This configuration prioritizes compactness and strict parsing, disabling features like
     * pretty printing, lenient parsing, trailing commas, and comments. It is suitable for scenarios
     * where the output will be processed by machines or stored efficiently.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers.
     * @return A [kotlinx.serialization.json.Json] instance configured for machine-readable output.
     */
    override fun forMachines(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?) = Json {
        encodeDefaults = true
        ignoreUnknownKeys = false
        isLenient = false
        allowStructuredMapKeys = true
        prettyPrint = false
        explicitNulls = true
        coerceInputValues = false
        useArrayPolymorphism = false
        classDiscriminator = "type"
        allowSpecialFloatingPointValues = true
        decodeEnumsCaseInsensitive = false
        allowTrailingComma = false
        allowComments = false
        classDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC
        serializersModule = serializersModule(serializersModuleBuilder)
    }
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
 * Creates a new [kotlinx.serialization.json.Json] instance from the current one, based on the new modification
 *
 * @param builder Change settings from the current instance in the DSL
 * @return A [kotlinx.serialization.json.Json] instance configured according to the old and new settings.
 */
fun Json.modify(builder: JsonBuilder.() -> Unit) = Json(this, builder)