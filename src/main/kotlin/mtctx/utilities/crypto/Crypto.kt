/*
 *     Utilities: Crypto.kt
 *     Copyright (C) 2025 mtctx
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
