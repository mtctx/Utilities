/*
 * Utilities (Utilities.main): Effect.kt
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

package mtctx.utilities.effect

import kotlinx.coroutines.runBlocking
import mtctx.utilities.Outcome
import mtctx.utilities.runCatchingOutcomeOf

/**
 * An abstract representation of a computation that may produce a value or fail with an error.
 *
 * The [Effect] class encapsulates a suspendable computation (a "thunk") that produces a value of type [A].
 * It provides a functional programming interface for transforming, chaining, and handling errors in computations
 * in a type-safe manner. Subclasses, such as [IO], implement specific behaviors for executing the computation.
 *
 * @param A The type of the value produced by the computation.
 * @param thunk The suspendable computation that produces a value of type [A].
 * @see IO
 * @see Outcome
 */
abstract class Effect<A>(val thunk: suspend () -> A) {

    /**
     * Transforms the result of this [Effect] using the provided [transform] function.
     *
     * @param transform The function to transform the value of type [A] into a value of type [B].
     * @return A new [Effect] containing the transformed value.
     */
    abstract fun <B> map(transform: suspend (A) -> B): Effect<B>

    /**
     * Chains this [Effect] with another [Effect] using the provided [transform] function.
     *
     * @param transform The function that takes the value of type [A] and produces another [Effect] of type [B].
     * @return An [IO] containing the result of the chained computation.
     */
    abstract fun <B> flatMap(transform: suspend (A) -> Effect<B>): IO<B>

    /**
     * Handles errors in this [Effect] by recovering with a default value.
     *
     * @param recover The function that provides a fallback value of type [A] if the computation fails.
     * @return A new [Effect] that recovers from errors with the provided value.
     */
    abstract fun handleError(recover: suspend (Throwable) -> A): Effect<A>

    /**
     * Handles errors in this [Effect] by recovering with another [Effect].
     *
     * @param recover The function that provides a fallback [Effect] of type [A] if the computation fails.
     * @return A new [Effect] that recovers from errors with the provided [Effect].
     */
    abstract fun recoverWith(recover: suspend (Throwable) -> Effect<A>): Effect<A>

    /**
     * Applies a function contained in another [Effect] to the value of this [Effect].
     *
     * @param function An [Effect] containing a function that transforms a value of type [A] to type [B].
     * @return A new [Effect] containing the result of applying the function to the value.
     */
    abstract fun <B> ap(function: Effect<(A) -> B>): Effect<B>

    /**
     * Executes the computation synchronously, returning a default value on failure.
     *
     * @param defaultValueOnFailure The default value to return if the computation fails.
     * @param block A DSL block to customize the handling of success and failure outcomes.
     * @return The result of the computation, or the default value if it fails.
     */
    open fun runSync(defaultValueOnFailure: A, block: suspend RunDSL<A>.() -> Unit = {}): A =
        runSync({ defaultValueOnFailure }, block)

    /**
     * Executes the computation synchronously, using a suspendable default value provider on failure.
     *
     * @param defaultValueOnFailure A suspendable function that provides the default value if the computation fails.
     * @param block A DSL block to customize the handling of success and failure outcomes.
     * @return The result of the computation, or the default value if it fails.
     */
    open fun runSync(defaultValueOnFailure: suspend () -> A, block: suspend RunDSL<A>.() -> Unit = {}): A =
        runBlocking { run(defaultValueOnFailure, block) }

    /**
     * Executes the computation asynchronously, returning a default value on failure.
     *
     * @param defaultValueOnFailure The default value to return if the computation fails.
     * @param block A DSL block to customize the handling of success and failure outcomes.
     * @return The result of the computation, or the default value if it fails.
     */
    open suspend fun run(defaultValueOnFailure: A, block: suspend RunDSL<A>.() -> Unit = {}): A =
        run({ defaultValueOnFailure }, block)

    /**
     * Executes the computation asynchronously, using a suspendable default value provider on failure.
     *
     * @param defaultValueOnFailure A suspendable function that provides the default value if the computation fails.
     * @param block A DSL block to customize the handling of success and failure outcomes.
     * @return The result of the computation, or the default value if it fails.
     */
    open suspend fun run(
        defaultValueOnFailure: suspend () -> A,
        block: suspend RunDSL<A>.() -> Unit = {}
    ): A {
        val (onSuccess, onFailure) = RunDSL(defaultValueOnFailure).apply { block() }.build()
        return when (val outcome = runCatchingOutcomeOf<A> { thunk() }) {
            is Outcome.Success -> onSuccess(outcome)
            is Outcome.Failure -> onFailure(outcome)
        }
    }

    /**
     * Converts this [Effect] into an [Effect] that produces a [Unit] value.
     *
     * @return A new [Effect] that executes the same computation but discards the result.
     */
    abstract fun void(): Effect<Unit>

    /**
     * A DSL for configuring the behavior of [run] and [runSync] methods.
     *
     * @param A The type of the value produced by the computation.
     * @param defaultValueOnFailure A suspendable function that provides the default value on failure.
     * @param successful A function to handle successful outcomes.
     * @param failed A function to handle failed outcomes.
     */
    class RunDSL<A>(
        private var defaultValueOnFailure: suspend () -> A,
        private var successful: suspend (Outcome.Success<A>) -> A = { it.value },
        private var failed: suspend (Outcome.Failure) -> A = { _ -> defaultValueOnFailure() }
    ) {
        /**
         * Constructs a [RunDSL] with a constant default value.
         *
         * @param defaultValueOnFailure The default value to use on failure.
         */
        constructor(defaultValueOnFailure: A) : this({ defaultValueOnFailure })

        /**
         * Sets the default value provider for failed computations.
         *
         * @param block A suspendable function that provides the default value.
         * @return This [RunDSL] instance for chaining.
         */
        fun defaultValueOnFailure(block: suspend () -> A) = apply { defaultValueOnFailure = block }

        /**
         * Configures the handling of successful outcomes.
         *
         * @param block A suspendable function that processes a successful outcome.
         * @return This [RunDSL] instance for chaining.
         */
        fun successful(block: suspend Outcome.Success<A>.() -> A) = apply { successful = block }

        /**
         * Configures the handling of failed outcomes.
         *
         * @param block A suspendable function that processes a failed outcome.
         * @return This [RunDSL] instance for chaining.
         */
        fun failed(block: suspend Outcome.Failure.() -> A) = apply { failed = block }

        /**
         * Builds the configuration into a pair of success and failure handlers.
         *
         * @return A pair of suspendable functions for handling success and failure outcomes.
         */
        fun build() = successful to failed
    }

    /**
     * A companion interface for creating instances of [Effect] subclasses.
     *
     * This interface defines a factory method for creating an [Effect] that wraps a pure value,
     * enabling subclasses like [IO] to provide a way to create effects without side effects.
     *
     * @param E The specific [Effect] subclass.
     * @see Effect
     * @see IO
     */
    interface Companion<E : Effect<*>> {

        /**
         * Creates an [Effect] that wraps a pure value.
         *
         * @param value The value to wrap in an [Effect].
         * @return An [Effect] instance containing the provided value.
         */
        fun <A> pure(value: A): E
    }
}