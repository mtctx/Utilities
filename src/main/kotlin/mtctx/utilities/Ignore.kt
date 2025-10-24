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

/**
 * Marks code elements that should be ignored by the reader.
 *
 * This annotation is used to indicate that certain classes, constructors, properties, parameters, or variables
 * should be excluded from processing, such as serialization, deserialization, or other user-defined operations.
 * The [reason] provides context for why the element should be ignored, using values from [IgnoreReason].
 *
 * For example, this can be used to exclude fields from JSON serialization or to mark parameters that should
 * not be considered in certain workflows.
 *
 * @param reason The reason for ignoring the annotated element.
 * @see IgnoreReason
 */
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

/**
 * Lists reasons for marking code elements to be ignored by the reader, as used by the [Ignore] annotation.
 *
 * Each reason includes a detailed description to clarify why an element should be excluded from processing
 * or serialization.
 *
 * @param detailed A human-readable description of the reason.
 */
enum class IgnoreReason(val detailed: String) {
    /** Code or feature is not used in the current context. */
    UNUSED("Code or feature is not used anywhere"),

    /** Old code kept for backward compatibility but should not be processed. */
    LEGACY("Old code kept for backward compatibility"),

    /** Feature is not implemented yet and should be ignored. */
    NOT_IMPLEMENTED("Feature is not implemented yet"),

    /** Temporary workaround or placeholder that should be excluded. */
    TEMPORARY("Temporary workaround or placeholder"),

    /** Feature is experimental and should not be processed by users. */
    EXPERIMENTAL("Feature is experimental and may change"),

    /** Feature only relevant for a specific platform and should be ignored elsewhere. */
    PLATFORM_SPECIFIC("Feature only relevant for a specific platform"),

    /** Used only in debug builds and should be ignored in production. */
    DEBUG_ONLY("Used only in debug builds"),

    /** Used only for performance testing and should be ignored in normal use. */
    PERFORMANCE_TEST("Used only for performance testing"),

    /** Only exists for documentation purposes and should be ignored in processing. */
    DOCUMENTATION("Only exists for documentation purposes"),

    /** Feature intended only for internal use and should not be processed by users. */
    INTERNAL_USE_ONLY("Feature intended only for internal use, not public"),

    /** Code requires a security review and should be ignored until reviewed. */
    SECURITY_REVIEW_PENDING("Code requires a security review before it can be enabled"),

    /** Feature depends on third-party code or library and should be ignored in certain contexts. */
    THIRD_PARTY_DEPENDENCY("Feature depends on third-party code or library");

    /**
     * Returns the detailed description of the reason.
     *
     * @return The description of the ignore reason.
     */
    override fun toString() = detailed
}

/**
 * An exception thrown when an element marked with [Ignore] is accessed or processed, indicating the [reason].
 *
 * @param reason The [IgnoreReason] explaining why the element should be ignored.
 */
class IgnoredException(reason: IgnoreReason) : Exception(reason.toString())

/**
 * Returns the specified [IgnoreReason] for use in code or annotations.
 *
 * @param reason The [IgnoreReason] to return.
 * @return The specified [IgnoreReason].
 * @see Ignore
 */
fun ignore(reason: IgnoreReason): IgnoreReason = reason

/**
 * Throws an [IgnoredException] with the specified [reason].
 *
 * @param reason The [IgnoreReason] explaining why the element should be ignored.
 * @throws IgnoredException Always thrown with the specified reason.
 */
fun ignoreAndThrow(reason: IgnoreReason): Nothing = throw IgnoredException(reason)