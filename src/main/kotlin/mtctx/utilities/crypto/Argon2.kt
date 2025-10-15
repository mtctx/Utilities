/*
 *     Utilities: Argon2.kt
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

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters

class Argon2 {
    companion object {
        private const val ITERATIONS = 4
        private const val MEM_LIMIT = 131072
        private const val HASH_LENGTH = 64
        private const val PARALLELISM = 2
        private const val SALT_SIZE = 16

        private val argon2Builder = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withIterations(ITERATIONS)
            .withMemoryAsKB(MEM_LIMIT)
            .withParallelism(PARALLELISM)

        @JvmStatic
        fun generateSalt(size: Int = SALT_SIZE): ByteArray {
            val salt = ByteArray(size)
            SECURE_RANDOM.nextBytes(salt)
            return salt
        }

        @JvmStatic
        fun hash(input: String, salt: ByteArray = generateSalt()): Result {
            argon2Builder.withSalt(salt)
            val generator = Argon2BytesGenerator()
            generator.init(argon2Builder.build())
            val result = ByteArray(HASH_LENGTH)
            generator.generateBytes(input.toByteArray(), result)
            return Result(result, salt)
        }

        @JvmStatic
        fun verify(input: String, hash: ByteArray, salt: ByteArray): Boolean =
            hash(input, salt).hash.contentEquals(hash)

        fun String.hashWithArgon2(salt: ByteArray = generateSalt()): Result = hash(this, salt)
        fun String.verifyWithArgon2(hash: ByteArray, salt: ByteArray): Boolean = verify(this, hash, salt)
        fun ByteArray.verifyWithArgon2(unhashed: String, salt: ByteArray): Boolean = verify(unhashed, this, salt)
    }

    class Result(val hash: ByteArray, val salt: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Result

            if (!hash.contentEquals(other.hash)) return false
            if (!salt.contentEquals(other.salt)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = hash.contentHashCode()
            result = 31 * result + salt.contentHashCode()
            return result
        }
    }
}