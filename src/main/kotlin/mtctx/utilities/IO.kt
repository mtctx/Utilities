/*
 * Utilities (Utilities.main): IO.kt
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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import mtctx.utilities.serialization.jsonForMachines
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import java.io.File

val fileSystem = FileSystem.SYSTEM

fun <T> T.serialize(serializer: KSerializer<T>, format: StringFormat = jsonForMachines): String =
    format.encodeToString(serializer, this)

fun <T> String.deserialize(deserializer: KSerializer<T>, format: StringFormat = jsonForMachines): T =
    format.decodeFromString(deserializer, this)

fun <T> T.serializeAndWrite(
    serializer: KSerializer<T>,
    path: Path,
    append: Boolean = true,
    atomicMove: Boolean = false,
    format: StringFormat = jsonForMachines
): T {
    path.parent?.let { fileSystem.createDirectories(it) }
    val writablePath = if (atomicMove) path.sibling("${path.name}.temp") else path

    if (append)
        fileSystem.appendingSink(writablePath).buffer().use { it.writeUtf8(serialize(serializer, format)) }
    else fileSystem.write(writablePath) { writeUtf8(serialize(serializer, format)) }

    if (atomicMove && writablePath != path)
        fileSystem.atomicMove(writablePath, path)

    return this
}

fun <T> Path.readAndDeserialize(deserializer: KSerializer<T>, format: StringFormat = jsonForMachines): T =
    fileSystem.read(this) { readUtf8() }.deserialize(deserializer, format)

fun <T> File.readAndDeserialize(deserializer: KSerializer<T>, format: StringFormat = jsonForMachines): T =
    inputStream().bufferedReader().readText().deserialize(deserializer, format)

fun Path.sibling(name: String): Path =
    parent?.div(name) ?: name.toPath()
