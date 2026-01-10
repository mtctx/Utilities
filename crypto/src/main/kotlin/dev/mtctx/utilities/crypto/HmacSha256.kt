@file:Suppress("unused")

package dev.mtctx.utilities.crypto

import dev.mtctx.utilities.crypto.HmacSha256.Companion.KEY_SIZE
import dev.mtctx.utilities.crypto.HmacSha256.Companion.generateKey
import org.bouncycastle.crypto.Mac
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter

/**
 * Provides utilities for generating and verifying HMAC-SHA256 message authentication codes.
 *
 * HMAC-SHA256 is a keyed-hash message authentication code that uses the SHA-256 hash function.
 * This implementation uses the Bouncy Castle library to compute HMAC-SHA256 tags for input data
 * and verify their integrity. It includes methods for generating keys, computing HMACs, and verifying tags.
 *
 * The [Companion] object contains static methods for key generation, HMAC computation, and verification,
 * along with extension functions for convenient use with [String] and [ByteArray]. The [Result] class
 * encapsulates the HMAC tag and the key used for easy handling.
 *
 * @see Argon2 for password hashing utilities
 * @see SECURE_RANDOM for the random number generator used for key generation
 */
class HmacSha256 {
    companion object {
        /** Default size of the HMAC key, in bytes (default: 32). */
        private const val KEY_SIZE = 32

        /**
         * Generates a random key for use in HMAC-SHA256 computation.
         *
         * @param size The size of the key in bytes (default: [KEY_SIZE]).
         * @return A [ByteArray] containing the generated key.
         */
        @JvmStatic
        fun generateKey(size: Int = KEY_SIZE): ByteArray {
            val key = ByteArray(size)
            SECURE_RANDOM.nextBytes(key)
            return key
        }

        /**
         * Generates an HMAC-SHA256 tag for an input string using the specified or generated key.
         *
         * @param input The input string to generate the HMAC for.
         * @param key The key to use for HMAC computation (default: a newly generated key via [generateKey]).
         * @return A [Result] containing the HMAC tag and the key used.
         */
        @JvmStatic
        fun generate(input: String, key: ByteArray = generateKey()): Result {
            val hmac: Mac = HMac(SHA256Digest())
            hmac.init(KeyParameter(key))

            val inputBytes = input.toByteArray(Charsets.UTF_8)
            hmac.update(inputBytes, 0, inputBytes.size)

            val result = ByteArray(hmac.macSize)
            hmac.doFinal(result, 0)

            return Result(result, key)
        }

        /**
         * Verifies an input string against a provided HMAC-SHA256 tag and key.
         *
         * @param input The input string to verify.
         * @param tag The expected HMAC tag to compare against.
         * @param key The key used to generate the HMAC tag.
         * @return `true` if the input matches the tag, `false` otherwise.
         */
        @JvmStatic
        fun verify(input: String, tag: ByteArray, key: ByteArray): Boolean =
            generate(input, key).tag.secureEquals(tag)

        /**
         * Generates an HMAC-SHA256 tag for this string using the specified or generated key.
         *
         * @param key The key to use for HMAC computation (default: a newly generated key via [generateKey]).
         * @return A [Result] containing the HMAC tag and the key used.
         */
        fun String.generateMacWithHmacSha256(key: ByteArray = generateKey()): Result = generate(this, key)

        /**
         * Verifies this string against a provided HMAC-SHA256 tag and key.
         *
         * @param tag The expected HMAC tag to compare against.
         * @param key The key used to generate the HMAC tag.
         * @return `true` if this string matches the tag, `false` otherwise.
         */
        fun String.verifyMacWithHmacSha256(tag: ByteArray, key: ByteArray): Boolean = verify(this, tag, key)

        /**
         * Verifies an input string against this HMAC-SHA256 tag and the provided key.
         *
         * @param unhashed The input string to verify.
         * @param key The key used to generate this HMAC tag.
         * @return `true` if the input matches this tag, `false` otherwise.
         */
        fun ByteArray.verifyMacWithHmacSha256(unhashed: String, key: ByteArray): Boolean = verify(unhashed, this, key)
    }

    /**
     * Encapsulates the result of an HMAC-SHA256 operation, containing the tag and the key used.
     *
     * @param tag The generated HMAC tag as a [ByteArray].
     * @param key The key used for HMAC computation as a [ByteArray].
     */
    class Result(val tag: ByteArray, val key: ByteArray) {
        /**
         * Compares this [Result] with another object for equality.
         * Two [Result] objects are equal if their tags and keys are identical.
         *
         * @param other The object to compare with.
         * @return `true` if the objects are equal, `false` otherwise.
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Result

            if (!tag.contentEquals(other.tag)) return false
            if (!key.contentEquals(other.key)) return false

            return true
        }

        /**
         * Generates a hash code for this [Result] based on its [tag] and [key].
         *
         * @return The hash code.
         */
        override fun hashCode(): Int {
            var result = tag.contentHashCode()
            result = 31 * result + key.contentHashCode()
            return result
        }
    }
}