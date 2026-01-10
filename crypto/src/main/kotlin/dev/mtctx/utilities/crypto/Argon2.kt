package dev.mtctx.utilities.crypto

import dev.mtctx.utilities.Outcome
import dev.mtctx.utilities.crypto.Argon2.Companion.SALT_SIZE
import dev.mtctx.utilities.crypto.Argon2.Companion.generateSalt
import dev.mtctx.utilities.datasizes.mib
import dev.mtctx.utilities.failure
import dev.mtctx.utilities.mapCatching
import dev.mtctx.utilities.success
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import org.bouncycastle.util.Arrays
import kotlin.io.encoding.Base64

/**
 * Provides utilities for password hashing and verification using the Argon2 algorithm (ARGON2id variant).
 *
 * Argon2 is a memory-hard password-hashing function designed to resist brute-force attacks.
 * This implementation uses the Bouncy Castle library and provides methods for generating salts,
 * hashing passwords, and verifying hashes. It uses predefined parameters for iterations, memory limit,
 * parallelism, and hash length to balance security and performance.
 *
 * The [Companion] object contains static methods for hashing and verification, along with extension
 * functions for convenient use with [String] and [ByteArray]. The [Result] class encapsulates the
 * hash and salt for easy handling.
 *
 * @see HmacSha256 for HMAC-SHA256-based message authentication
 * @see SECURE_RANDOM for the random number generator used for salt generation
 */
class Argon2 {
    companion object {
        private val HASH_PATTERN =
            "\$argon2id\$v=(\\d+)\$m=(\\d+),t=(\\d+),p=(\\d+)$([a-zA-Z0-9+/=]+)$([a-zA-Z0-9+/=]+)$".toRegex()

        /** Number of iterations for the Argon2 algorithm (default: 4). */
        private const val ITERATIONS = 4

        /** Memory limit for the Argon2 algorithm, in kibibytes (default: 128 MiB). */
        private val MEM_LIMIT = 128.mib.toInt()

        /** Length of the generated hash, in bytes (default: 64). */
        private const val HASH_LENGTH = 64

        /** Number of parallel threads for the Argon2 algorithm (default: 2). */
        private const val PARALLELISM = 2

        /** Default size of the salt, in bytes (default: 16). */
        private const val SALT_SIZE = 16

        /** Configured Argon2 parameters builder for the ARGON2id variant. */
        private val argon2Builder = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withIterations(ITERATIONS)
            .withMemoryAsKB(MEM_LIMIT)
            .withParallelism(PARALLELISM)

        /**
         * Generates a random salt for use in Argon2 hashing.
         *
         * @param size The size of the salt in bytes (default: [SALT_SIZE]).
         * @return A [ByteArray] containing the generated salt.
         */
        @JvmStatic
        fun generateSalt(size: Int = SALT_SIZE): ByteArray {
            val salt = ByteArray(size)
            SECURE_RANDOM.nextBytes(salt)
            return salt
        }

        /**
         * Hashes an input string using the Argon2 algorithm with the specified or generated salt.
         *
         * @param input The input string to hash (e.g., a password).
         * @param salt The salt to use for hashing (default: a newly generated salt via [generateSalt]).
         * @return A [Result] containing the hash and the salt used.
         */
        @JvmStatic
        fun hash(
            input: String,
            salt: ByteArray = generateSalt(),
            builder: Argon2Parameters.Builder = argon2Builder
        ): Result {
            builder.withSalt(salt)
            val generator = Argon2BytesGenerator()
            generator.init(builder.build())
            val result = ByteArray(HASH_LENGTH)
            generator.generateBytes(input.toByteArray(), result)
            return Result(result, salt)
        }

        /**
         * Verifies an input string against a provided hash and salt using the Argon2 algorithm.
         *
         * @param input The input string to verify (e.g., a password).
         * @param hash The expected hash to compare against.
         * @param salt The salt used to generate the hash.
         * @return `true` if the input matches the hash, `false` otherwise.
         */
        @JvmStatic
        suspend fun verify(input: String, hash: ByteArray): Outcome<Boolean> =
            parseHashString(hash).mapCatching { parsed ->
                val builder = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                    .withIterations(parsed.iterations)
                    .withMemoryAsKB(parsed.memory)
                    .withSalt(parsed.salt)
                    .withParallelism(parsed.parallelism)

                val generator = Argon2BytesGenerator()
                generator.init(builder.build())
                val generatedHash = ByteArray(parsed.hash.size)
                generator.generateBytes(input.toByteArray(), generatedHash)

                Arrays.constantTimeAreEqual(parsed.hash, generatedHash)
            }

        private fun parseHashString(encodedHash: ByteArray): Outcome<ParsedHash> {
            val matchResult = HASH_PATTERN.find(encodedHash.decodeToString())
                ?: return failure("Invalid Argon2 hash format: $encodedHash")

            val (versionStr, memoryStr, iterationsStr, parallelismStr, saltStr, hashStr) = matchResult.destructured

            val version = versionStr.toInt()
            val memory = memoryStr.toInt()
            val iterations = iterationsStr.toInt()
            val parallelism = parallelismStr.toInt()
            val salt = Base64.decode(saltStr)
            val hash = Base64.decode(hashStr)

            return success(ParsedHash(version, memory, iterations, parallelism, salt, hash))
        }

        /**
         * Hashes this string using the Argon2 algorithm with the specified or generated salt.
         *
         * @param salt The salt to use for hashing (default: a newly generated salt via [generateSalt]).
         * @return A [Result] containing the hash and the salt used.
         */
        fun String.hashWithArgon2(salt: ByteArray = generateSalt()): Result = hash(this, salt)

        /**
         * Verifies this string against a provided hash and salt using the Argon2 algorithm.
         *
         * @param hash The expected hash to compare against.
         * @param salt The salt used to generate the hash.
         * @return `true` if this string matches the hash, `false` otherwise.
         */
        suspend fun String.verifyWithArgon2(hash: ByteArray): Outcome<Boolean> = verify(this, hash)

        /**
         * Verifies an input string against this hash and the provided salt using the Argon2 algorithm.
         *
         * @param unhashed The input string to verify (e.g., a password).
         * @param salt The salt used to generate this hash.
         * @return `true` if the input matches this hash, `false` otherwise.
         */
        suspend fun ByteArray.verifyWithArgon2(unhashed: String): Outcome<Boolean> = verify(unhashed, this)
    }

    /**
     * Encapsulates the result of an Argon2 hashing operation, containing the hash and the salt used.
     *
     * @param hash The generated hash as a [ByteArray].
     * @param salt The salt used for hashing as a [ByteArray].
     */
    class Result(val hash: ByteArray, val salt: ByteArray) {
        /**
         * Compares this [Result] with another object for equality.
         * Two [Result] objects are equal if their hashes and salts are identical.
         *
         * @param other The object to compare with.
         * @return `true` if the objects are equal, `false` otherwise.
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Result

            if (!hash.contentEquals(other.hash)) return false
            if (!salt.contentEquals(other.salt)) return false

            return true
        }

        /**
         * Generates a hash code for this [Result] based on its [hash] and [salt].
         *
         * @return The hash code.
         */
        override fun hashCode(): Int {
            var result = hash.contentHashCode()
            result = 31 * result + salt.contentHashCode()
            return result
        }
    }


    private data class ParsedHash(
        val version: Int,
        val memory: Int,
        val iterations: Int,
        val parallelism: Int,
        val salt: ByteArray,
        val hash: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ParsedHash

            if (version != other.version) return false
            if (memory != other.memory) return false
            if (iterations != other.iterations) return false
            if (parallelism != other.parallelism) return false
            if (!salt.contentEquals(other.salt)) return false
            if (!hash.contentEquals(other.hash)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = version
            result = 31 * result + memory
            result = 31 * result + iterations
            result = 31 * result + parallelism
            result = 31 * result + salt.contentHashCode()
            result = 31 * result + hash.contentHashCode()
            return result
        }
    }
}