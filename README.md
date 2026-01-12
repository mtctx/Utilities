# Utilities

A collection of high-quality Kotlin utility modules designed for common development tasks, including cryptography, data
sizes, functional effects, robust error handling, and more.

## Modules

| Module                                     |                                Description                                 | Version |                        Dependencies                         |
|:-------------------------------------------|:--------------------------------------------------------------------------:|---------|:-----------------------------------------------------------:|
| [`bytearray`](bytearray/README.md)         |  Utility functions for simplified `ByteArray` operations (hex, padding).   | 2.0.0   |                            None                             |
| [`crypto`](crypto/README.md)               |  High-level cryptographic utilities (Argon2, HMAC, secure data handling).  | 2.0.0   |     `bytearray`, `datasizes`, `outcome`, `bouncycastle`     |
| [`datasizes`](datasizes/README.md)         |    Type-safe data size constants and conversions (Binary and Decimal).     | 2.0.0   |                            None                             |
| [`effect`](effect/README.md)               |  Functional side-effect management (`Effect`, `IO`, coroutines support).   | 2.0.0   |               `outcome`, `kotlinx-coroutines`               |
| [`ignore`](ignore/README.md)               |    Annotation to mark elements to be ignored by processors or readers.     | 2.0.0   |                            None                             |
| [`io`](io/README.md)                       |          Efficient IO and file system utilities powered by Okio.           | 2.0.0   | `serialization`, `outcome`, `okio`, `kotlinx-serialization` |
| [`outcome`](outcome/README.md)             | Type-safe `Success`/`Failure` result handling for robust error management. | 2.1.0   |                            None                             |
| [`serialization`](serialization/README.md) |          Kotlin Serialization extensions and common serializers.           | 2.1.0   |    `outcome`, `kotlinx-serialization`, `kotlin-reflect`     |

## Installation

To use any of these modules, add the corresponding dependency to your project. Replace `<module>` with the module name
and `<version>` with the latest version (e.g., `2.0.0`).

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-<module>:<version>")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-<module>:<version>'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-&lt;module&gt;</artifactId>
    <version>&lt;version&gt;</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-&lt;module&gt;" rev="&lt;version&gt;"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-&lt;module&gt;" % "&lt;version&gt;"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-<module> "<version>"]
```

</details>

## Implementation Guidelines

To implement a new module in this repository, follow these guidelines:

1. **Naming**: Use a concise, lowercase name for the module directory.
2. **Structure**: Follow the standard Gradle/Kotlin project structure:
   `src/main/kotlin/dev/mtctx/utilities/<module-name>/`.
3. **Gradle Configuration**:
    - Use the convention plugins for Dokka and Publishing.
    - Set the `archivesName` in the `base` block.
    - Define the `version` and `group` (should be `dev.mtctx.utilities`).
4. **Documentation**: Each module should have a `README.md` and KDoc for public APIs.
5. **Testing**: Include unit tests for all public functionality in `src/test/kotlin`.
