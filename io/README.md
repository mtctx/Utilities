# io module

The `io` module provides comprehensive IO and file system utilities, building on top of Okio for high-performance data
handling. It simplifies common file operations, stream management, and integrates seamlessly with the `serialization`
and `outcome` modules for robust data persistence and retrieval.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-io:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-io:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-io</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-io" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-io" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-io "2.0.0"]
```

</details>

## Usage Examples

### Serialization and File Writing

Easily write objects to files using Kotlin Serialization and Okio.

```kotlin
val data = MyData(name = "Test", value = 123)
val path = "path/to/file.json".toPath()

// Serialize and write to file
data.serializeAndWrite(MyData.serializer(), path)
```

### Reading and Deserialization

Read and deserialize objects back from files.

```kotlin
val path = "path/to/file.json".toPath()
val outcome = path.readAndDeserialize(MyData.serializer())

outcome.onSuccess { data ->
    println("Read data: ${data.name}")
}
```

## Dependencies

### Internal

- `serialization` (implementation)
- `outcome` (implementation)

### External

- `org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0-RC` (implementation)
- `com.squareup.okio:okio:3.16.2` (api - exposed)
