/*
 * Utilities (Utilities.main): NumberConversionIsUnsafeAndCouldCauseBufferOverflow.kt
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
 * Marks number conversion operations that may be unsafe due to potential overflow.
 *
 * This annotation is used on properties or functions that perform large number conversions,
 * particularly for petabyte- or exabyte-scale calculations, where standard [Int], [Long], or [Double]
 * types may exceed their maximum values, leading to overflow or loss of precision.
 *
 * Developers are encouraged to use corresponding safe alternatives (e.g., properties suffixed with `Safe`)
 * that utilize [java.math.BigInteger] or [java.math.BigDecimal] for handling large values without overflow.
 *
 * @see NumberConversionIsUnsafeAndCouldCauseBufferOverflow
 */
@RequiresOptIn(
    message = "Number conversion is unsafe and could cause buffer overflow",
    level = RequiresOptIn.Level.WARNING
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.RUNTIME)
annotation class NumberConversionIsUnsafeAndCouldCauseBufferOverflow