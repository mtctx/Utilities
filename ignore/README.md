# ignore module

The `ignore` module provides an `@Ignore` annotation and related utilities to explicitly mark elements that should be
skipped by certain processors or readers. This is particularly useful for documentation tools, code analysis, or any
custom logic that needs to identify and exclude "ignored" components.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-ignore:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-ignore:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-ignore</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-ignore" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-ignore" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-ignore "2.0.0"]
```

</details>

## Usage Examples

### Using @Ignore Annotation

Mark classes or functions to be ignored with an optional reason.

```kotlin
@Ignore(reason = "Deprecated and replaced by NewService")
class OldService {
    // ...
}

@Ignore
fun internalHelper() {
    // ...
}
```

## Dependencies

This module has no external or internal dependencies.
