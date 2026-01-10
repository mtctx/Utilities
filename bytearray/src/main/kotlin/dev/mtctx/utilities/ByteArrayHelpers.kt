@file:Suppress("unused")

package dev.mtctx.utilities

/**
 * Prepends a prefix [ByteArray] to this [ByteArray].
 *
 * Creates a new [ByteArray] by concatenating the [prefix] followed by this array's contents.
 *
 * @param prefix The [ByteArray] to prepend.
 * @return A new [ByteArray] containing the prefix followed by this array's contents.
 */
fun ByteArray.prepend(prefix: ByteArray): ByteArray {
    val result = ByteArray(prefix.size + this.size)
    System.arraycopy(prefix, 0, result, 0, prefix.size)
    System.arraycopy(this, 0, result, prefix.size, this.size)
    return result
}

/**
 * Pads this [ByteArray] to the specified length with zeros if it is too short.
 *
 * If the array's length is less than [len], a new [ByteArray] is created with additional zero bytes.
 * If the array's length is equal to or greater than [len], the original array is returned unchanged.
 *
 * @param len The desired length of the resulting [ByteArray].
 * @return A [ByteArray] of at least the specified length, padded with zeros if necessary.
 */
fun ByteArray.padTo(len: Int) = if (size < len) this + ByteArray(len - size) else this