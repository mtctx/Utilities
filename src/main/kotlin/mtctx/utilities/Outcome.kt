/*
 * Utilities (Utilities.main): Outcome.kt
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

package mtctx.utilities

import mtctx.utilities.Outcome.Failure
import mtctx.utilities.Outcome.Success

/**
 * Represents the result of an operation that can either succeed with a value or fail with an error.
 *
 * This sealed interface provides a robust way to handle operations that may produce a value or an error,
 * offering a type-safe alternative to exceptions or nullable types. It includes utility functions for
 * mapping, recovering, and folding outcomes, as well as converting to and from [Result].
 *
 * @param T The type of the value in a successful outcome.
 * @see Success
 * @see Failure
 */
sealed interface Outcome<out T> {
    /** Indicates whether the operation succeeded. */
    val succeeded: Boolean

    /** Indicates whether the operation failed (opposite of [succeeded]). */
    val failed: Boolean get() = !succeeded

    /**
     * Represents a successful outcome with a value.
     *
     * @param value The value produced by the successful operation.
     */
    @JvmInline
    value class Success<T>(val value: T) : Outcome<T> {
        override val succeeded: Boolean get() = true

        /**
         * Returns a string representation of this [Success] instance.
         *
         * @return A string in the format "Success(value=$value, type=$type)".
         */
        override fun toString(): String = "Success(value=$value, type=${value!!.javaClass.name})"
    }

    /**
     * Represents a failed outcome with an error message and optional cause.
     *
     * @param message A description of the failure.
     * @param throwable An optional [Throwable] that caused the failure.
     * @param outcome An optional nested [Outcome] for chained failures.
     */
    data class Failure(val message: String, val throwable: Throwable? = null, val outcome: Outcome<*>? = null) :
        Outcome<Nothing> {
        override val succeeded: Boolean get() = false

        /**
         * Returns a string representation of this [Failure] instance.
         *
         * Includes the message and, if present, the cause's class name.
         *
         * @return A string in the format "Failure(message=$message, cause=$cause)".
         */
        override fun toString(): String =
            "Failure(message=$message" + if (throwable != null) ", cause=${throwable.javaClass.name})" else ")"

        /**
         * Returns a string representation of this [Failure] instance with stack trace details.
         *
         * Includes the message and, if present, the full stack trace of the cause.
         *
         * @return A string in the format "Failure(message=$message, stacktrace=$stacktrace)".
         */
        fun toStringWithStackTrace(): String =
            "Failure(message=$message" + if (throwable != null) ", stacktrace=${throwable.stackTraceToString()})" else ")"
    }

    /**
     * Returns the value of a [Success] or `null` if the outcome is a [Failure].
     *
     * @return The value if successful, or `null` if failed.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    /**
     * Returns the [Throwable] of a [Failure] or `null` if the outcome is a [Success].
     *
     * @return The throwable if failed, or `null` if successful.
     */
    fun exceptionOrNull(): Throwable? = when (this) {
        is Success -> null
        is Failure -> throwable
    }

    /**
     * Converts this [Outcome] to a [Result].
     *
     * @return A [Result] containing the value if successful, or an exception if failed.
     */
    fun toResult(): Result<T> = when (this) {
        is Success -> Result.success(value)
        is Failure -> Result.failure(throwable ?: Exception(message))
    }
}

/**
 * Maps the value of a successful [Outcome] using the provided [transform] function.
 *
 * @param transform The function to transform the value of a [Success].
 * @return A new [Outcome] with the transformed value if successful, or the original [Failure].
 */
suspend inline fun <T, R> Outcome<T>.map(transform: suspend (T) -> R): Outcome<R> = when (this) {
    is Success -> Success(transform(value))
    is Failure -> this
}

/**
 * Flat-maps the value of a successful [Outcome] using the provided [transform] function.
 *
 * @param transform The function to transform the value of a [Success] into another [Outcome].
 * @return The [Outcome] produced by [transform] if successful, or the original [Failure].
 */
suspend inline fun <T, R> Outcome<T>.flatMap(transform: suspend (T) -> Outcome<R>): Outcome<R> = when (this) {
    is Success -> transform(value)
    is Failure -> this
}

/**
 * Maps the value of a successful [Outcome] using the provided [transform] function, catching any exceptions.
 *
 * @param transform The function to transform the value of a [Success].
 * @return A new [Outcome] with the transformed value if successful, or a [Failure] if an exception occurs.
 */
suspend inline fun <T, R> Outcome<T>.mapCatching(transform: suspend (T) -> R): Outcome<R> = when (this) {
    is Success ->
        try {
            Success(transform(value))
        } catch (t: Throwable) {
            Failure("Exception in mapCatching", t)
        }

    is Failure -> this
}

/**
 * Maps a [Failure] using the provided [transform] function, leaving [Success] unchanged.
 *
 * @param transform The function to transform a [Failure].
 * @return The transformed [Failure] if the outcome is a failure, or the original [Success].
 */
suspend inline fun <T> Outcome<T>.mapFailure(transform: suspend (Failure) -> Failure): Outcome<T> = when (this) {
    is Success -> this
    is Failure -> transform(this)
}

/**
 * Folds the [Outcome] into a single value by applying [onSuccess] for a [Success] or [onFailure] for a [Failure].
 *
 * @param onSuccess The function to apply to the value of a [Success].
 * @param onFailure The function to apply to the message and throwable of a [Failure].
 * @return The result of applying the appropriate function.
 */
suspend inline fun <T, R> Outcome<T>.fold(
    onSuccess: suspend (T) -> R,
    onFailure: suspend (String, Throwable?) -> R
): R = when (this) {
    is Success -> onSuccess(value)
    is Failure -> onFailure(message, throwable)
}

/**
 * Recovers from a [Failure] by applying the [recoverBlock] function to produce a new value.
 *
 * @param recoverBlock The function to produce a value from a [Failure].
 * @return The original [Success], or a new [Success] with the recovered value.
 */
suspend inline fun <T> Outcome<T>.recover(recoverBlock: suspend (Failure) -> T): Outcome<T> = when (this) {
    is Success -> this
    is Failure -> success(recoverBlock(this))
}

/**
 * Recovers from a [Failure] by applying the [recoverBlock] function, catching any exceptions.
 *
 * @param recoverBlock The function to produce a value from a [Failure].
 * @return The original [Success], or a new [Success] with the recovered value, or a [Failure] if an exception occurs.
 */
suspend inline fun <T> Outcome<T>.recoverCatching(recoverBlock: suspend (Failure) -> T): Outcome<T> = when (this) {
    is Success -> this
    is Failure ->
        try {
            success(recoverBlock(this))
        } catch (t: Throwable) {
            failure("Exception in recoverCatching", t)
        }
}

/**
 * Converts this [Result] to an [Outcome].
 *
 * @return An [Outcome] containing the value if successful, or a [Failure] if failed.
 */
fun <T> Result<T>.toOutcome(): Outcome<T> = fold(::success, ::failure)

/**
 * Executes the [block] and wraps the result in an [Outcome].
 *
 * @param block The function to execute.
 * @return A [Success] with the result of [block], or a [Failure] if an exception occurs.
 */
suspend inline fun <T> runCatchingOutcomeOf(block: suspend () -> T): Outcome<T> =
    try {
        success(block())
    } catch (e: Throwable) {
        failure(e)
    }

/**
 * Executes the [block] that returns an [Outcome], catching any exceptions.
 *
 * @param block The function that returns an [Outcome].
 * @return The [Outcome] from [block], or a [Failure] if an exception occurs.
 */
suspend inline fun <T> runCatchingOutcomeBlock(block: suspend () -> Outcome<T>): Outcome<T> =
    try {
        block()
    } catch (e: Throwable) {
        failure(e)
    }

/**
 * Executes the [block] and wraps the result in an [Outcome], using this value as context.
 *
 * @param block The function to execute.
 * @return A [Success] with the result of [block], or a [Failure] if an exception occurs.
 */
suspend inline fun <T> T.runCatchingOutcomeOf(block: suspend () -> T): Outcome<T> =
    try {
        success(block())
    } catch (t: Throwable) {
        failure("Exception in runCatching Outcome", t)
    }

/**
 * Executes the [block] that returns an [Outcome], using this value as context, catching any exceptions.
 *
 * @param block The function that returns an [Outcome].
 * @return The [Outcome] from [block], or a [Failure] if an exception occurs.
 */
suspend inline fun <T> T.runCatchingOutcomeBlock(block: suspend () -> Outcome<T>): Outcome<T> =
    try {
        block()
    } catch (e: Throwable) {
        failure(e)
    }

/**
 * Creates a [Success] with a `true` value.
 *
 * @return A [Success] instance containing `true`.
 */
fun success(): Success<Boolean> = Success(true)

/**
 * Creates a [Success] with the specified [value].
 *
 * @param value The value to wrap in a [Success].
 * @return A [Success] instance containing the value.
 */
fun <T> success(value: T): Success<T> = Success(value)

/**
 * Executes the [block] and wraps the result in a [Success].
 *
 * @param block The function to execute.
 * @return A [Success] instance containing the result of [block].
 */
suspend inline fun <T> success(block: suspend () -> T): Success<T> {
    return Success(block())
}

/**
 * Creates a [Failure] with the specified [message], [throwable], and optional nested [outcome].
 *
 * @param message A description of the failure.
 * @param throwable An optional [Throwable] that caused the failure.
 * @param outcome An optional nested [Outcome] for chained failures.
 * @return A [Failure] instance.
 */
fun failure(message: String, throwable: Throwable? = null, outcome: Outcome<*>? = null): Failure =
    Failure(message, throwable, outcome)

/**
 * Creates a [Failure] with the specified [throwable] and optional nested [outcome].
 *
 * @param throwable The [Throwable] that caused the failure.
 * @param outcome An optional nested [Outcome] for chained failures.
 * @return A [Failure] instance with a default message.
 */
fun failure(throwable: Throwable, outcome: Outcome<*>? = null): Failure = Failure("No message provided.", throwable)

/**
 * Creates a [Failure] with an optional nested [outcome].
 *
 * @param outcome An optional nested [Outcome] for chained failures.
 * @return A [Failure] instance with a default message.
 */
fun failure(outcome: Outcome<*>? = null): Failure = Failure("No message provided.", outcome = outcome)