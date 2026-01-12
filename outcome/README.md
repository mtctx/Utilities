# outcome module

The `outcome` module provides a type-safe `Outcome` type, representing either a `Success` or a `Failure`. This pattern
encourages robust error handling and explicit management of failure states without relying on exceptions for control
flow, leading to more resilient and maintainable applications.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-outcome:2.1.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-outcome:2.1.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-outcome</artifactId>
    <version>2.1.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-outcome" rev="2.1.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-outcome" % "2.1.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-outcome "2.1.0"]
```

</details>

## Usage Examples

### Creating Outcomes

Create successful or failed results easily.

```kotlin
fun divide(a: Int, b: Int): Outcome<Int> = if (b == 0) {
    failure("Cannot divide by zero")
} else {
    success(a / b)
}
```

## Dependencies

This module has no external or internal dependencies.
