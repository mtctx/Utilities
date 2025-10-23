/*
 * Utilities (Utilities.main): Ignore.kt
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

package mtctx.utilities

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Ignore(
    val reason: IgnoreReason
)

enum class IgnoreReason(val detailed: String) {
    UNUSED("Code or feature is not used anywhere"),
    LEGACY("Old code kept for backward compatibility"),
    NOT_IMPLEMENTED("Feature is not implemented yet"),
    TEMPORARY("Temporary workaround or placeholder"),
    EXPERIMENTAL("Feature is experimental and may change"),
    PLATFORM_SPECIFIC("Feature only relevant for a specific platform"),
    DEBUG_ONLY("Used only in debug builds"),
    PERFORMANCE_TEST("Used only for performance testing"),
    DOCUMENTATION("Only exists for documentation purposes"),
    INTERNAL_USE_ONLY("Feature intended only for internal use, not public"),
    SECURITY_REVIEW_PENDING("Code requires a security review before it can be enabled"),
    THIRD_PARTY_DEPENDENCY("Feature depends on third-party code or library");

    override fun toString() = detailed
}

class IgnoredException(reason: IgnoreReason) : Exception(reason.toString())

fun ignore(reason: IgnoreReason): IgnoreReason = reason
fun ignoreAndyThrow(reason: IgnoreReason): Nothing = throw IgnoredException(reason)