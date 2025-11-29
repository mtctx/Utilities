/*
 * Utilities (Utilities.main): Crypto.kt
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

package mtctx.utilities.crypto

import mtctx.utilities.padTo
import org.bouncycastle.util.Arrays
import java.security.SecureRandom

/**
 * A secure random number generator used for cryptographic operations, such as generating salts and keys.
 */
val SECURE_RANDOM = SecureRandom()

/**
 * Compares this [ByteArray] with another in constant time to prevent timing attacks.
 *
 * This method ensures that the comparison takes the same amount of time regardless of the input,
 * making it suitable for cryptographic operations where timing attacks are a concern.
 * The arrays are padded to the same length before comparison to handle unequal sizes securely.
 *
 * @param other The [ByteArray] to compare with.
 * @return `true` if the arrays are equal, `false` otherwise.
 * @see secureEquals
 */
fun ByteArray.constantTimeEquals(other: ByteArray): Boolean {
    val length = maxOf(size, other.size)
    return Arrays.constantTimeAreEqual(padTo(length), other.padTo(length))
}

/**
 * Infix function to compare this [ByteArray] with another in constant time.
 *
 * This is a convenience wrapper around [constantTimeEquals] for more readable code.
 *
 * @param other The [ByteArray] to compare with.
 * @return `true` if the arrays are equal, `false` otherwise.
 * @see constantTimeEquals
 */
infix fun ByteArray.secureEquals(other: ByteArray): Boolean = constantTimeEquals(other)