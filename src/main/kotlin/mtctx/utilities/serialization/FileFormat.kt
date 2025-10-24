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

/**
 * Abstract base class for defining serialization formats that produce [StringFormat] instances.
 *
 * This class provides a framework for creating serialization formats tailored for human-readable or
 * machine-readable output. It supports custom serialization modules through [serializersModuleBuilders],
 * which are used to configure serializers for specific types, such as [UUID] via [UUIDSerializer].
 *
 * Subclasses must implement [forHumans] and [forMachines] to provide format-specific configurations.
 * The [defaultForHumans] and [defaultForMachines] properties offer default instances without additional
 * serializers, while [serializersModule] builds a [SerializersModule] from the provided builders.
 *
 * @param T The specific [StringFormat] type produced by this format (e.g., [Json]).
 * @param serializersModuleBuilders A mutable set of builders to configure custom serializers.
 * @see JsonFileFormat for a concrete implementation using JSON
 * @see UUIDSerializer for the default UUID serializer
 */
abstract class FileFormat<T : StringFormat>(protected val serializersModuleBuilders: MutableSet<SerializersModuleBuilder.() -> Unit>) {
    init {
        this.serializersModuleBuilders.add(serializersModuleBuilder { contextual(UUID::class, UUIDSerializer) })
    }

    /**
     * Builds a [SerializersModule] from the provided [serializersModuleBuilder] and the default builders.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers to the module.
     * @return A [SerializersModule] containing all configured serializers.
     */
    protected fun serializersModule(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?): SerializersModule =
        SerializersModule {
            serializersModuleBuilder?.let { serializersModuleBuilders.add(it) }
            serializersModuleBuilders.forEach { it() }
        }

    /**
     * Provides a default [StringFormat] instance configured for human-readable output.
     * Lazily initialized by calling [forHumans] with no additional serializers.
     */
    val defaultForHumans by lazy { forHumans(null) }

    /**
     * Provides a default [StringFormat] instance configured for machine-readable output.
     * Lazily initialized by calling [forMachines] with no additional serializers.
     */
    val defaultForMachines by lazy { forMachines(null) }

    /**
     * Creates a [StringFormat] instance configured for human-readable output.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers.
     * @return A [T] instance configured for human-readable serialization.
     */
    abstract fun forHumans(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?): T

    /**
     * Creates a [StringFormat] instance configured for machine-readable output.
     *
     * @param serializersModuleBuilder An optional builder to add custom serializers.
     * @return A [T] instance configured for machine-readable serialization.
     */
    abstract fun forMachines(serializersModuleBuilder: (SerializersModuleBuilder.() -> Unit)?): T
}