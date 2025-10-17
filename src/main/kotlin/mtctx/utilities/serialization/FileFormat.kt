/*
 * Utilities (Utilities.main): FileFormat.kt
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

import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import mtctx.utilities.serialization.serializer.UUIDSerializer
import mtctx.utilities.serializersModuleBuilder
import java.util.*

abstract class FileFormat<T : StringFormat>(protected val serializersModuleBuilders: MutableSet<SerializersModuleBuilder.() -> Unit>) {
    init {
        this.serializersModuleBuilders.add(serializersModuleBuilder { contextual(UUID::class, UUIDSerializer) })
    }

    protected fun serializersModule(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?): SerializersModule =
        SerializersModule {
            serializersModuleBuilder?.let { serializersModuleBuilders.add(it) }
            serializersModuleBuilders.forEach { it() }
        }

    val defaultForHumans by lazy { forHumans(null) }
    val defaultForMachines by lazy { forMachines(null) }

    abstract fun forHumans(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?): T
    abstract fun forMachines(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?): T
}