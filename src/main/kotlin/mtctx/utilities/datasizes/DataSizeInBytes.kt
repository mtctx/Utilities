/*
 * Utilities (Utilities.main): DataSizeInBytes.kt
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

package mtctx.utilities.datasizes

/**
 * Defines standard data size units in bytes, providing constants for common storage measurements.
 * Implementations of this interface specify whether the units follow binary (base-1024) or decimal (base-1000) conventions.
 * These constants are useful for calculations involving file sizes, memory, or network data.
 */
interface DataSizeInBytes {
    /** Number of bytes in a kilobyte (KB or KiB, depending on the implementation). */
    val kilobyte: Long

    /** Number of bytes in a megabyte (MB or MiB, depending on the implementation). */
    val megabyte: Long

    /** Number of bytes in a gigabyte (GB or GiB, depending on the implementation). */
    val gigabyte: Long

    /** Number of bytes in a terabyte (TB or TiB, depending on the implementation). */
    val terabyte: Long

    /** Number of bytes in a petabyte (PB or PiB, depending on the implementation). */
    val petabyte: Long

    /** Number of bytes in an exabyte (EB or EiB, depending on the implementation). */
    val exabyte: Long
}