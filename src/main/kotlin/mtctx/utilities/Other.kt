/*
 * Utilities (Utilities.main): Other.kt
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
@file:Suppress("unused")

package mtctx.utilities

import kotlinx.serialization.modules.SerializersModuleBuilder

/**
 * Prepends a prefix [ByteArray] to this [ByteArray].
 *
 * Creates a new [ByteArray] by concatenating the [prefix] followed by this array's contents.
 *
 * @param prefix The [ByteArray] to prepend.
 * @return A new [ByteArray] containing the prefix followed by this array's contents.
 */
fun ByteArray.prepend(prefix: ByteArray): ByteArray {
    val result = ByteArray(prefix.size + this.size)
    System.arraycopy(prefix, 0, result, 0, prefix.size)
    System.arraycopy(this, 0, result, prefix.size, this.size)
    return result
}

/**
 * Pads this [ByteArray] to the specified length with zeros if it is too short.
 *
 * If the array's length is less than [len], a new [ByteArray] is created with additional zero bytes.
 * If the array's length is equal to or greater than [len], the original array is returned unchanged.
 *
 * @param len The desired length of the resulting [ByteArray].
 * @return A [ByteArray] of at least the specified length, padded with zeros if necessary.
 */
fun ByteArray.padTo(len: Int) = if (size < len) this + ByteArray(len - size) else this

/**
 * Abstract base class for types that can be serialized to a [ByteArray].
 *
 * Subclasses must implement the [serialize] method to define how their data is converted
 * to a [ByteArray] representation.
 */
abstract class CustomByteArraySerializable {
    /**
     * Serializes this object to a [ByteArray].
     *
     * @return A [ByteArray] representing the serialized form of this object.
     */
    abstract fun serialize(): ByteArray
    abstract fun deserialize(byteArray: ByteArray): CustomByteArraySerializable
}

/**
 * Wraps a [SerializersModuleBuilder] lambda for use in serialization configurations.
 *
 * This function is a utility to pass a [SerializersModuleBuilder] lambda to classes like [mtctx.utilities.serialization.FileFormat],
 * ensuring the lambda can be stored and applied to configure serializers.
 *
 * @param serializersModuleBuilder The lambda to configure a [SerializersModuleBuilder].
 * @return The same lambda, typed for use in serialization configurations.
 * @see mtctx.utilities.serialization.FileFormat
 */
fun serializersModuleBuilder(serializersModuleBuilder: SerializersModuleBuilder.() -> Unit): SerializersModuleBuilder.() -> Unit =
    serializersModuleBuilder