/*
 * Utilities (Utilities.main): DecimalDataSize.kt
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

package mtctx.utilities.datasizes

import java.math.BigDecimal
import java.math.BigInteger

/**
 * Provides decimal (base-1000) data size constants, following SI standards (e.g., KB, MB).
 * This is typically used for file systems, network speeds, and other contexts where base-1000 units are standard.
 *
 * The constants are defined using powers of 1000 for accurate decimal scaling.
 * Extension properties (e.g., [kb], [mb]) are provided for convenient conversion of [Int], [Long], and [Double]
 * values to their respective decimal data sizes.
 *
 * For petabyte ([pb]) and exabyte ([eb]) conversions, use the `Safe` variants (e.g., [pbSafe], [ebSafe])
 * to avoid overflow risks with large numbers.
 *
 * @see BinaryDataSize for binary (base-1024) units
 * @see DataSizeInBytes for the interface defining these constants
 */
object DecimalDataSize : DataSizeInBytes {
    override val kilobyte = 1000L
    override val megabyte = 1000L * kilobyte
    override val gigabyte = 1000L * megabyte
    override val terabyte = 1000L * gigabyte
    override val petabyte = 1000L * terabyte
    override val exabyte = 1000L * petabyte
}

/**
 * Converts a [Double] to kilobytes (KB, 1000 bytes).
 * @return The value multiplied by [DecimalDataSize.kilobyte].
 */
val Double.kb: Double get() = this * DecimalDataSize.kilobyte

/**
 * Converts an [Int] to kilobytes (KB, 1000 bytes).
 * @return The value multiplied by [DecimalDataSize.kilobyte].
 */
val Int.kb: Long get() = this * DecimalDataSize.kilobyte

/**
 * Converts a [Long] to kilobytes (KB, 1000 bytes).
 * @return The value multiplied by [DecimalDataSize.kilobyte].
 */
val Long.kb: Long get() = this * DecimalDataSize.kilobyte

/**
 * Converts a [Double] to megabytes (MB, 1000^2 bytes).
 * @return The value multiplied by [DecimalDataSize.megabyte].
 */
val Double.mb: Double get() = this * DecimalDataSize.megabyte

/**
 * Converts an [Int] to megabytes (MB, 1000^2 bytes).
 * @return The value multiplied by [DecimalDataSize.megabyte].
 */
val Int.mb: Long get() = this * DecimalDataSize.megabyte

/**
 * Converts a [Long] to megabytes (MB, 1000^2 bytes).
 * @return The value multiplied by [DecimalDataSize.megabyte].
 */
val Long.mb: Long get() = this * DecimalDataSize.megabyte

/**
 * Converts a [Double] to gigabytes (GB, 1000^3 bytes).
 * @return The value multiplied by [DecimalDataSize.gigabyte].
 */
val Double.gb: Double get() = this * DecimalDataSize.gigabyte

/**
 * Converts an [Int] to gigabytes (GB, 1000^3 bytes).
 * @return The value multiplied by [DecimalDataSize.gigabyte].
 */
val Int.gb: Long get() = this * DecimalDataSize.gigabyte

/**
 * Converts a [Long] to gigabytes (GB, 1000^3 bytes).
 * @return The value multiplied by [DecimalDataSize.gigabyte].
 */
val Long.gb: Long get() = this * DecimalDataSize.gigabyte

/**
 * Converts a [Double] to terabytes (TB, 1000^4 bytes).
 * @return The value multiplied by [DecimalDataSize.terabyte].
 */
val Double.tb: Double get() = this * DecimalDataSize.terabyte

/**
 * Converts an [Int] to terabytes (TB, 1000^4 bytes).
 * @return The value multiplied by [DecimalDataSize.terabyte].
 */
val Int.tb: Long get() = this * DecimalDataSize.terabyte

/**
 * Converts a [Long] to terabytes (TB, 1000^4 bytes).
 * @return The value multiplied by [DecimalDataSize.terabyte].
 */
val Long.tb: Long get() = this * DecimalDataSize.terabyte

/**
 * Converts a [Double] to petabytes (PB, 1000^5 bytes).
 *
 * **Warning**: This operation may cause overflow or loss of precision for large values.
 * Use [pbSafe] for safe calculations with [BigDecimal].
 *
 * @return The value multiplied by [DecimalDataSize.petabyte].
 * @see pbSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Double.pb: Double get() = this * DecimalDataSize.petabyte

/**
 * Safely converts a [Double] to petabytes (PB, 1000^5 bytes) using [BigDecimal].
 * @return The value multiplied by [DecimalDataSize.petabyte] as a [BigDecimal].
 */
val Double.pbSafe: BigDecimal get() = toBigDecimal().multiply(DecimalDataSize.petabyte.toBigDecimal())

/**
 * Converts an [Int] to petabytes (PB, 1000^5 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [pbSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [DecimalDataSize.petabyte].
 * @see pbSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Int.pb: Long get() = this * DecimalDataSize.petabyte

/**
 * Safely converts an [Int] to petabytes (PB, 1000^5 bytes) using [BigInteger].
 * @return The value multiplied by [DecimalDataSize.petabyte] as a [BigInteger].
 */
val Int.pbSafe: BigInteger get() = this.toBigInteger().multiply(DecimalDataSize.petabyte.toBigInteger())

/**
 * Converts a [Long] to petabytes (PB, 1000^5 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [pbSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [DecimalDataSize.petabyte].
 * @see pbSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Long.pb: Long get() = this * DecimalDataSize.petabyte

/**
 * Safely converts a [Long] to petabytes (PB, 1000^5 bytes) using [BigInteger].
 * @return The value multiplied by [DecimalDataSize.petabyte] as a [BigInteger].
 */
val Long.pbSafe: BigInteger get() = toBigInteger().multiply(DecimalDataSize.petabyte.toBigInteger())

/**
 * Converts a [Double] to exabytes (EB, 1000^6 bytes).
 *
 * **Warning**: This operation may cause overflow or loss of precision for large values.
 * Use [ebSafe] for safe calculations with [BigDecimal].
 *
 * @return The value multiplied by [DecimalDataSize.exabyte].
 * @see ebSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Double.eb: Double get() = this * DecimalDataSize.exabyte

/**
 * Safely converts a [Double] to exabytes (EB, 1000^6 bytes) using [BigDecimal].
 * @return The value multiplied by [DecimalDataSize.exabyte] as a [BigDecimal].
 */
val Double.ebSafe: BigDecimal get() = toBigDecimal().multiply(DecimalDataSize.exabyte.toBigDecimal())

/**
 * Converts an [Int] to exabytes (EB, 1000^6 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [ebSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [DecimalDataSize.exabyte].
 * @see ebSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Int.eb: Long get() = this * DecimalDataSize.exabyte

/**
 * Safely converts an [Int] to exabytes (EB, 1000^6 bytes) using [BigInteger].
 * @return The value multiplied by [DecimalDataSize.exabyte] as a [BigInteger].
 */
val Int.ebSafe: BigInteger get() = this.toBigInteger().multiply(DecimalDataSize.exabyte.toBigInteger())

/**
 * Converts a [Long] to exabytes (EB, 1000^6 bytes).
 *
 * **Warning**: This operation may cause overflow for large values.
 * Use [ebSafe] for safe calculations with [BigInteger].
 *
 * @return The value multiplied by [DecimalDataSize.exabyte].
 * @see ebSafe
 */
@NumberConversionIsUnsafeAndCouldCauseBufferOverflow
val Long.eb: Long get() = this * DecimalDataSize.exabyte

/**
 * Safely converts a [Long] to exabytes (EB, 1000^6 bytes) using [BigInteger].
 * @return The value multiplied by [DecimalDataSize.exabyte] as a [BigInteger].
 */
val Long.ebSafe: BigInteger get() = toBigInteger().multiply(DecimalDataSize.exabyte.toBigInteger())