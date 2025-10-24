/*
 * Utilities (Utilities.main): Argon2.kt
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

import mtctx.utilities.crypto.Argon2.Companion.SALT_SIZE
import mtctx.utilities.crypto.Argon2.Companion.generateSalt
import mtctx.utilities.datasizes.mib
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters

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
        fun hash(input: String, salt: ByteArray = generateSalt()): Result {
            argon2Builder.withSalt(salt)
            val generator = Argon2BytesGenerator()
            generator.init(argon2Builder.build())
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
        fun verify(input: String, hash: ByteArray, salt: ByteArray): Boolean =
            hash(input, salt).hash.contentEquals(hash)

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
        fun String.verifyWithArgon2(hash: ByteArray, salt: ByteArray): Boolean = verify(this, hash, salt)

        /**
         * Verifies an input string against this hash and the provided salt using the Argon2 algorithm.
         *
         * @param unhashed The input string to verify (e.g., a password).
         * @param salt The salt used to generate this hash.
         * @return `true` if the input matches this hash, `false` otherwise.
         */
        fun ByteArray.verifyWithArgon2(unhashed: String, salt: ByteArray): Boolean = verify(unhashed, this, salt)
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
}