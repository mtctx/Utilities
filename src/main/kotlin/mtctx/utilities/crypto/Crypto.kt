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

package mtctx.utilities.crypto

import mtctx.utilities.padTo
import org.bouncycastle.util.Arrays
import java.security.SecureRandom

val SECURE_RANDOM = SecureRandom()

fun ByteArray.constantTimeEquals(other: ByteArray): Boolean {
    val length = maxOf(size, other.size)
    return Arrays.constantTimeAreEqual(padTo(length), other.padTo(length))
}

infix fun ByteArray.secureEquals(other: ByteArray): Boolean = constantTimeEquals(other)
