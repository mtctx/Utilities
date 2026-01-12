# bytearray module

The `bytearray` module provides a set of utility functions to simplify common operations with `ByteArray`. It includes
helpers for hexadecimal conversions, padding, and other byte-level manipulations.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-bytearray:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-bytearray:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-bytearray</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-bytearray" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-bytearray" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-bytearray "2.0.0"]
```

</details>

## Usage Examples

### Hexadecimal Conversion

Convert byte arrays to hex strings and vice-versa easily.

```kotlin
val bytes = byteArrayOf(0xDE.toByte(), 0xAD.toByte(), 0xBE.toByte(), 0xEF.toByte())
val hexString = bytes.toHexString() // "deadbeef"

val decodedBytes = "deadbeef".hexToByteArray()
```

### Padding

Pad byte arrays to a certain length.

```kotlin
val original = byteArrayOf(1, 2, 3)
val padded = original.padTo(8) // Pads with zeros to length 8
```

## Dependencies

This module has no external or internal dependencies.
