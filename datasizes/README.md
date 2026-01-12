# datasizes module

The `datasizes` module offers a type-safe and intuitive way to work with data sizes (e.g., bytes, kilobytes, megabytes).
It provides constants and extension properties for both binary (IEC, power of 2) and decimal (SI, power of 10) systems,
helping to avoid manual calculation errors and significantly improving code readability.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-datasizes:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-datasizes:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-datasizes</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-datasizes" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-datasizes" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-datasizes "2.0.0"]
```

</details>

## Usage Examples

### Working with Binary Sizes (IEC)

Use extension properties on numbers for clear data size definitions.

```kotlin
val cacheSize = 512.mib // 512 MiB
val bufferSize = 4.kib // 4 KiB
val maxStorage = 2.tib // 2 TiB

val bytes = cacheSize.inBytes // Get the raw Long value
```

### Working with Decimal Sizes (SI)

Standard decimal sizes are also supported.

```kotlin
val diskSpace = 1.tb // 1 TB (10^12 bytes)
val downloadLimit = 500.mb // 500 MB (500 * 10^6 bytes)
```

## Dependencies

This module has no external or internal dependencies.
