/*
 * Utilities (Utilities.main): Serialization.kt
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
@file:OptIn(ExperimentalSerializationApi::class)

package mtctx.utilities.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModuleBuilder

internal val jsonFileFormat = JsonFileFormat()
val jsonForHumans = jsonFileFormat.defaultForHumans
val jsonForMachines = jsonFileFormat.defaultForMachines

@Suppress("UNCHECKED_CAST")
fun <T : StringFormat> stringFormat(
    forHumans: Boolean = false,
    fileFormat: FileFormat,
    serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)? = null
): T {
    val formatFunction = if (forHumans) fileFormat::forHumans else fileFormat::forMachines
    return formatFunction(serializersModuleBuilder) as T
}

fun json(forHumans: Boolean = false, serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)? = null): Json =
    stringFormat(forHumans, jsonFileFormat, serializersModuleBuilder)