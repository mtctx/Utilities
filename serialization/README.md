# serialization module

The `serialization` module extends Kotlin Serialization with additional utilities and common serializers. It provides a
consistent API for handling various file formats (like JSON) and simplifies the process of configuring and using
serializers across your project, ensuring standard serialization behavior.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-serialization:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-serialization:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-serialization</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-serialization" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-serialization" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-serialization "2.0.0"]
```

</details>

## Usage Examples

### Using Configured JSON Formats

Pre-configured JSON formats for human or machine readability.

```kotlin
val humanJson = json(forHumans = true)
val machineJson = json(forHumans = false)

val jsonString = humanJson.encodeToString(myData)
```

### Custom Serializers Module

Easily add custom serializers using the provided builder.

```kotlin
val myJson = json {
    contextual(UUID::class, UUIDSerializer)
}
```

## Dependencies

### Internal

- `outcome` (implementation)

### External

- `org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0-RC` (implementation)
- `org.jetbrains.kotlin:kotlin-reflect:2.3.0` (implementation)
