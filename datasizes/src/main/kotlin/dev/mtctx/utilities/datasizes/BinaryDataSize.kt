@file:Suppress("unused")

package dev.mtctx.utilities.datasizes

import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides binary (base-1024) data size constants, following IEC standards (e.g., KiB, MiB).
 * This is typically used for memory-related calculations, such as RAM or certain software contexts.
 *
 * The constants are defined using bit-shifting for powers of 1024, ensuring accurate binary scaling.
 * Extension properties (e.g., [kib], [mib]) are provided for convenient conversion of [Int], [Long], and [Double]
 * values to their respective binary data sizes.
 *
 * For petabyte ([pib]) and exabyte ([eib]) conversions, use the `Safe` variants (e.g., [pibSafe], [eibSafe])
 * to avoid overflow risks with large numbers.
 *
 * @see DecimalDataSize for decimal (base-1000) units
 * @see DataSizeInBytes for the interface defining these constants
 */
object BinaryDataSize : DataSizeInBytes {
    override val kilobyte = 1L shl 10 // 1024
    override val megabyte = kilobyte shl 10 // 1024^2
    override val gigabyte = megabyte shl 10 // 1024^3
    override val terabyte = gigabyte shl 10 // 1024^4
    override val petabyte = terabyte shl 10 // 1024^5
    override val exabyte = petabyte shl 10 // 1024^6
}

/**
 * Converts a [Double] to kibibytes (KiB, 1024 bytes).
 * @return The value multiplied by [BinaryDataSize.kilobyte].
 */
val Double.kib: Double get() = this * BinaryDataSize.kilobyte

/**
 * Converts an [Int] to kibibytes (KiB, 1024 bytes).
 * @return The value multiplied by [BinaryDataSize.kilobyte].
 */
val Int.kib: Long get() = this * BinaryDataSize.kilobyte

/**
 * Converts a [Long] to kibibytes (KiB, 1024 bytes).
 * @return The value multiplied by [BinaryDataSize.kilobyte].
 */
val Long.kib: Long get() = this * BinaryDataSize.kilobyte

/**
 * Converts a [Double] to mebibytes (MiB, 1024^2 bytes).
 * @return The value multiplied by [BinaryDataSize.megabyte].
 */
val Double.mib: Double get() = this * BinaryDataSize.megabyte

/**
 * Converts an [Int] to mebibytes (MiB, 1024^2 bytes).
 * @return The value multiplied by [BinaryDataSize.megabyte].
 */
val Int.mib: Long get() = this * BinaryDataSize.megabyte

/**
 * Converts a [Long] to mebibytes (MiB, 1024^2 bytes).
 * @return The value multiplied by [BinaryDataSize.megabyte].
 */
val Long.mib: Long get() = this * BinaryDataSize.megabyte

/**
 * Converts a [Double] to gibibytes (GiB, 1024^3 bytes).
 * @return The value multiplied by [BinaryDataSize.gigabyte].
 */
val Double.gib: Double get() = this * BinaryDataSize.gigabyte

/**
 * Converts an [Int] to gibibytes (GiB, 1024^3 bytes).
 * @return The value multiplied by [BinaryDataSize.gigabyte].
 */
val Int.gib: Long get() = this * BinaryDataSize.gigabyte

/**
 * Converts a [Long] to gibibytes (GiB, 1024^3 bytes).
 * @return The value multiplied by [BinaryDataSize.gigabyte].
 */
val Long.gib: Long get() = this * BinaryDataSize.gigabyte

/**
 * Converts a [Double] to tebibytes (TiB, 1024^4 bytes).
 * @return The value multiplied by [BinaryDataSize.terabyte].
 */
val Double.tib: Double get() = this * BinaryDataSize.terabyte

/**
 * Converts an [Int] to tebibytes (TiB, 1024^4 bytes).
 * @return The value multiplied by [BinaryDataSize.terabyte].
 */
val Int.tib: Long get() = this * BinaryDataSize.terabyte

/**
 * Converts a [Long] to tebibytes (TiB, 1024^4 bytes).
 * @return The value multiplied by [BinaryDataSize.terabyte].
 */
val Long.tib: Long get() = this * BinaryDataSize.terabyte

/**
 * Converts a [Double] to pebibytes (PiB, 1024^5 bytes).
 *
 * **Warning**: This operation may cause overflow or loss of precision for large values.
 * Use [pibSafe] for safe calculations with [BigDecimal].
 *
 * @return The value multiplied by [BinaryDataSize.petabyte].
 * @see pibSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Double.pib: Double get() = this * BinaryDataSize.petabyte

/**
 * Safely converts a [Double] to pebibytes (PiB, 1024^5 bytes) using [BigDecimal].
 * @return The value multiplied by [BinaryDataSize.petabyte] as a [BigDecimal].
 */
val Double.pibSafe: BigDecimal get() = toBigDecimal().multiply(BinaryDataSize.petabyte.toBigDecimal())

/**
 * Converts an [Int] to pebibytes (PiB, 1024^5 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [pibSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [BinaryDataSize.petabyte].
 * @see pibSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Int.pib: Long get() = this * BinaryDataSize.petabyte

/**
 * Safely converts an [Int] to pebibytes (PiB, 1024^5 bytes) using [BigInteger].
 * @return The value multiplied by [BinaryDataSize.petabyte] as a [BigInteger].
 */
val Int.pibSafe: BigInteger get() = this.toBigInteger().multiply(BinaryDataSize.petabyte.toBigInteger())

/**
 * Converts a [Long] to pebibytes (PiB, 1024^5 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [pibSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [BinaryDataSize.petabyte].
 * @see pibSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Long.pib: Long get() = this * BinaryDataSize.petabyte

/**
 * Safely converts a [Long] to pebibytes (PiB, 1024^5 bytes) using [BigInteger].
 * @return The value multiplied by [BinaryDataSize.petabyte] as a [BigInteger].
 */
val Long.pibSafe: BigInteger get() = toBigInteger().multiply(BinaryDataSize.petabyte.toBigInteger())

/**
 * Converts a [Double] to exbibytes (EiB, 1024^6 bytes).
 *
 * **Warning**: This operation may cause overflow or loss of precision for large values.
 * Use [eibSafe] for safe calculations with [BigDecimal].
 *
 * @return The value multiplied by [BinaryDataSize.exabyte].
 * @see eibSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Double.eib: Double get() = this * BinaryDataSize.exabyte

/**
 * Safely converts a [Double] to exbibytes (EiB, 1024^6 bytes) using [BigDecimal].
 * @return The value multiplied by [BinaryDataSize.exabyte] as a [BigDecimal].
 */
val Double.eibSafe: BigDecimal get() = toBigDecimal().multiply(BinaryDataSize.exabyte.toBigDecimal())

/**
 * Converts an [Int] to exbibytes (EiB, 1024^6 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [eibSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [BinaryDataSize.exabyte].
 * @see eibSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Int.eib: Long get() = this * BinaryDataSize.exabyte

/**
 * Safely converts an [Int] to exbibytes (EiB, 1024^6 bytes) using [BigInteger].
 * @return The value multiplied by [BinaryDataSize.exabyte] as a [BigInteger].
 */
val Int.eibSafe: BigInteger get() = this.toBigInteger().multiply(BinaryDataSize.exabyte.toBigInteger())

/**
 * Converts a [Long] to exbibytes (EiB, 1024^6 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [eibSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [BinaryDataSize.exabyte].
 * @see eibSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Long.eib: Long get() = this * BinaryDataSize.exabyte

/**
 * Safely converts a [Long] to exbibytes (EiB, 1024^6 bytes) using [BigInteger].
 * @return The value multiplied by [BinaryDataSize.exabyte] as a [BigInteger].
 */
val Long.eibSafe: BigInteger get() = toBigInteger().multiply(BinaryDataSize.exabyte.toBigInteger())