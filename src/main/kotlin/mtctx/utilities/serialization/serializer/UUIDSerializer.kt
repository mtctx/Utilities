/*
 * Utilities (Utilities.main): UUIDSerializer.kt
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

package mtctx.utilities.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * A [KSerializer] for serializing and deserializing [UUID] objects to and from strings.
 *
 * This serializer converts [UUID] instances to their string representation during serialization
 * and parses strings back to [UUID] instances during deserialization. It uses the standard
 * string format for UUIDs as defined by [UUID.toString] and [UUID.fromString].
 *
 * @see mtctx.utilities.serialization.FileFormat for where this serializer is registered by default
 */
object UUIDSerializer : KSerializer<UUID> {
    /**
     * The descriptor for the [UUID] type, represented as a string.
     */
    override val descriptor: SerialDescriptor = SerialDescriptor("UUID", String.serializer().descriptor)

    /**
     * Serializes a [UUID] to its string representation.
     *
     * @param encoder The [Encoder] to use for serialization.
     * @param value The [UUID] to serialize.
     */
    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    /**
     * Deserializes a string to a [UUID].
     *
     * @param decoder The [Decoder] to use for deserialization.
     * @return The [UUID] parsed from the input string.
     * @throws IllegalArgumentException if the input string is not a valid UUID.
     */
    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}