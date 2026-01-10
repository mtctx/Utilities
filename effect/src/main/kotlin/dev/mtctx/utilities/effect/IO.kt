@file:Suppress("unused")

package dev.mtctx.utilities.effect

/**
 * A concrete implementation of [Effect] for handling side-effecting computations.
 *
 * The [IO] class represents a computation that may perform side effects and produce a value of type [A].
 * It provides methods for transforming, chaining, and handling errors in a functional and type-safe manner.
 * Computations are lazily evaluated when executed using [run] or [runSync].
 *
 * @param A The type of the value produced by the computation.
 * @param thunk The suspendable computation that produces a value of type [A].
 * @see Effect
 * @see dev.mtctx.utilities.Outcome
 */
open class IO<A : Any>(thunk: suspend () -> A) : Effect<A>(thunk) {

    /**
     * Transforms the result of this [IO] using the provided [transform] function.
     *
     * @param transform The function to transform the value of type [A] into a value of type [B].
     * @return A new [IO] containing the transformed value.
     */
    override fun <B : Any> map(transform: suspend (A) -> B): IO<B> = IO { transform(thunk()) }

    /**
     * Chains this [IO] with another [Effect] using the provided [transform] function.
     *
     * @param transform The function that takes the value of type [A] and produces another [Effect] of type [B].
     * @return An [IO] containing the result of the chained computation.
     */
    override fun <B : Any> flatMap(transform: suspend (A) -> Effect<B>): IO<B> = IO {
        val next = transform(thunk())
        next.thunk()
    }

    /**
     * Handles errors in this [IO] by recovering with a default value.
     *
     * @param recover The function that provides a fallback value of type [A] if the computation fails.
     * @return A new [IO] that recovers from errors with the provided value.
     */
    override fun handleError(recover: suspend (Throwable) -> A): IO<A> = IO {
        try {
            thunk()
        } catch (t: Throwable) {
            recover(t)
        }
    }

    /**
     * Handles errors in this [IO] by recovering with another [Effect].
     *
     * @param recover The function that provides a fallback [Effect] of type [A] if the computation fails.
     * @return A new [IO] that recovers from errors with the provided [Effect].
     */
    override fun recoverWith(recover: suspend (Throwable) -> Effect<A>): IO<A> = IO {
        try {
            thunk()
        } catch (t: Throwable) {
            recover(t).thunk()
        }
    }

    /**
     * Applies a function contained in another [Effect] to the value of this [IO].
     *
     * @param function An [Effect] containing a function that transforms a value of type [A] to type [B].
     * @return A new [IO] containing the result of applying the function to the value.
     */
    override fun <B : Any> ap(function: Effect<(A) -> B>): IO<B> = IO {
        function.thunk().invoke(thunk())
    }

    /**
     * Converts this [IO] into an [IO] that produces a [Unit] value.
     *
     * @return A new [IO] that executes the same computation but discards the result.
     */
    override fun void(): IO<Unit> = IO { thunk() }

    companion object : Effect.Companion<IO<*>> {

        /**
         * Creates an [IO] that wraps a pure value.
         *
         * @param value The value to wrap in an [IO].
         * @return An [IO] instance containing the provided value.
         */
        override fun <A : Any> pure(value: A): IO<A> = IO { value }
    }
}

/**
 * Creates an [IO] instance from a suspendable computation.
 *
 * @param thunk The suspendable computation that produces a value of type [A].
 * @return An [IO] instance encapsulating the computation.
 */
fun <A : Any> io(thunk: suspend () -> A): IO<A> = IO(thunk)
fun <A : Any> io(value: A): IO<A> = IO.pure(value)