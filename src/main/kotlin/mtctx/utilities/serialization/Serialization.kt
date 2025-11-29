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
@file:Suppress("unused")
@file:OptIn(ExperimentalSerializationApi::class)

package mtctx.utilities.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModuleBuilder

/**
 * Default [JsonFileFormat] instance for JSON serialization.
 */
val jsonFileFormat = JsonFileFormat()

/**
 * Default [Json] instance configured for human-readable output, using [jsonFileFormat].
 */
val jsonForHumans = jsonFileFormat.defaultForHumans

/**
 * Default [Json] instance configured for machine-readable output, using [jsonFileFormat].
 */
val jsonForMachines = jsonFileFormat.defaultForMachines

/**
 * Creates a [StringFormat] instance based on the specified [fileFormat] and configuration.
 *
 * @param forHumans If `true`, configures the format for human-readable output; otherwise, for machine-readable output.
 * @param fileFormat The [FileFormat] instance to use for creating the [StringFormat].
 * @param serializersModuleBuilder An optional builder to add custom serializers.
 * @return A [T] instance configured according to the specified parameters.
 * @see FileFormat
 * @see JsonFileFormat
 */
fun <T : StringFormat> stringFormat(
    forHumans: Boolean = false,
    fileFormat: FileFormat<T>,
    serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)? = null
): T {
    val formatFunction = if (forHumans) fileFormat::forHumans else fileFormat::forMachines
    return formatFunction(serializersModuleBuilder)
}

/**
 * Creates a [Json] instance based on the [jsonFileFormat].
 *
 * @param forHumans If `true`, configures the JSON format for human-readable output; otherwise, for machine-readable output.
 * @param serializersModuleBuilder An optional builder to add custom serializers.
 * @return A [Json] instance configured according to the specified parameters.
 * @see JsonFileFormat
 * @see stringFormat
 */
fun json(forHumans: Boolean = false, serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)? = null): Json =
    stringFormat(forHumans, jsonFileFormat, serializersModuleBuilder)