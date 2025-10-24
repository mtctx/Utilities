/*
 * Utilities (Utilities.main): JsonFileFormat.kt
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

package mtctx.utilities.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModuleBuilder

/**
 * A [FileFormat] implementation for JSON serialization, providing configurations for human-readable
 * and machine-readable output.
 *
 * This class extends [FileFormat] to create [Json] instances with specific settings tailored for
 * human or machine use. The [forHumans] configuration includes features like pretty printing and
 * lenient parsing, while [forMachines] prioritizes compactness and strictness for efficient processing.
 * Both configurations support polymorphic serialization and custom serializers via [serializersModuleBuilders].
 *
 * @param serializersModuleBuilders A mutable set of builders to configure custom serializers (default: empty set).
 * @see FileFormat
 * @see Json
 */
@OptIn(ExperimentalSerializationApi::class)
class JsonFileFormat(serializersModuleBuilders: MutableSet<SerializersModuleBuilder.() -> Unit> = mutableSetOf()) :
    FileFormat<Json>(serializersModuleBuilders) {
    /**
     * Creates a [Json] instance configured for human-readable output.
     *
     * This configuration enables features like pretty printing, lenient parsing, trailing commas,
     * comments, and case-insensitive enum decoding. It is suitable for scenarios where the output
     * will be read or edited by humans.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers.
     * @return A [Json] instance configured for human-readable output.
     */
    override fun forHumans(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?) = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
        explicitNulls = true
        prettyPrintIndent = "  "
        coerceInputValues = true
        useArrayPolymorphism = false
        classDiscriminator = "type"
        allowSpecialFloatingPointValues = false
        decodeEnumsCaseInsensitive = true
        allowTrailingComma = true
        allowComments = true
        classDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC
        serializersModule = serializersModule(serializersModuleBuilder)
    }

    /**
     * Creates a [Json] instance configured for machine-readable output.
     *
     * This configuration prioritizes compactness and strict parsing, disabling features like
     * pretty printing, lenient parsing, trailing commas, and comments. It is suitable for scenarios
     * where the output will be processed by machines or stored efficiently.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers.
     * @return A [Json] instance configured for machine-readable output.
     */
    override fun forMachines(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?) = Json {
        encodeDefaults = true
        ignoreUnknownKeys = false
        isLenient = false
        allowStructuredMapKeys = true
        prettyPrint = false
        explicitNulls = true
        coerceInputValues = false
        useArrayPolymorphism = false
        classDiscriminator = "type"
        allowSpecialFloatingPointValues = true
        decodeEnumsCaseInsensitive = false
        allowTrailingComma = false
        allowComments = false
        classDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC
        serializersModule = serializersModule(serializersModuleBuilder)
    }
}