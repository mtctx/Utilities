/*
 *     Utilities: Serialization.kt
 *     Copyright (C) 2025 mtctx
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:OptIn(ExperimentalSerializationApi::class)

package mtctx.utilities

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json

val jsonForHumans: Json = Json {
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
}

val jsonForMachines: Json = Json {
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
}

fun <T : StringFormat> stringFormat(prettyPrint: Boolean = false, humanReadable: T, machineReadable: T): T =
    if (prettyPrint) humanReadable else machineReadable

fun json(prettyPrint: Boolean = false): Json = stringFormat(prettyPrint, jsonForHumans, jsonForMachines)