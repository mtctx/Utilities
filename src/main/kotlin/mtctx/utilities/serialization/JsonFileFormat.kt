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

import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import mtctx.utilities.serialization.serializer.UUIDSerializer
import java.util.*

class JsonFileFormat : FileFormat() {
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
        serializersModule = SerializersModule {
            contextual(UUID::class, UUIDSerializer())
            serializersModuleBuilder?.invoke(this)
        }
    }

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
        serializersModule = SerializersModule {
            contextual(UUID::class, UUIDSerializer())
            serializersModuleBuilder?.invoke(this)
        }
    }
}