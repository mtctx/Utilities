/*
 * Utilities (Utilities.main): Other.kt
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

import kotlinx.serialization.modules.SerializersModuleBuilder

fun ByteArray.prepend(prefix: ByteArray): ByteArray {
    val result = ByteArray(prefix.size + this.size)
    System.arraycopy(prefix, 0, result, 0, prefix.size)
    System.arraycopy(this, 0, result, prefix.size, this.size)
    return result
}

fun ByteArray.padTo(len: Int) = if (size < len) this + ByteArray(len - size) else this

abstract class CustomToByteArray {
    abstract fun serialize(): ByteArray
}

fun serializersModuleBuilder(serializersModuleBuilder: SerializersModuleBuilder.() -> Unit): SerializersModuleBuilder.() -> Unit =
    serializersModuleBuilder