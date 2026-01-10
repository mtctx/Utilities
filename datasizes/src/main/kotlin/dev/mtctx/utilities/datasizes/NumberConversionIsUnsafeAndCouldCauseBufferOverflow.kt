package dev.mtctx.utilities.datasizes

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