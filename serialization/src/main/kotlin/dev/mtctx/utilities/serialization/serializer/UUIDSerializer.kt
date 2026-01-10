package dev.mtctx.utilities.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * A [kotlinx.serialization.KSerializer] for serializing and deserializing [UUID] objects to and from strings.
 *
 * This serializer converts [UUID] instances to their string representation during serialization
 * and parses strings back to [UUID] instances during deserialization. It uses the standard
 * string format for UUIDs as defined by [UUID.toString] and [UUID.fromString].
 *
 * @see dev.mtctx.utilities.serialization.FileFormat for where this serializer is registered by default
 */
object UUIDSerializer : KSerializer<UUID> {
    /**
     * The descriptor for the [UUID] type, represented as a string.
     */
    override val descriptor: SerialDescriptor = SerialDescriptor("UUID", String.serializer().descriptor)

    /**
     * Serializes a [UUID] to its string representation.
     *
     * @param encoder The [kotlinx.serialization.encoding.Encoder] to use for serialization.
     * @param value The [UUID] to serialize.
     */
    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    /**
     * Deserializes a string to a [UUID].
     *
     * @param decoder The [kotlinx.serialization.encoding.Decoder] to use for deserialization.
     * @return The [UUID] parsed from the input string.
     * @throws IllegalArgumentException if the input string is not a valid UUID.
     */
    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}