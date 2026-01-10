@file:Suppress("unused")

package dev.mtctx.utilities.crypto

import dev.mtctx.utilities.padTo
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