# effect module

The `effect` module introduces functional programming abstractions for managing side effects in Kotlin. It provides an
`Effect` type and an `IO` system to handle asynchronous operations and error management in a declarative way,
facilitating cleaner, more predictable, and highly testable code.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-effect:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-effect:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-effect</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-effect" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-effect" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-effect "2.0.0"]
```

</details>

## Usage Examples

### Using IO for Side Effects

Encapsulate side effects and chain them using functional operators.

```kotlin
fun findUser(id: String): IO<User> = IO.suspend {
    // Database or API call
    User(id, "John Doe")
}

val program = findUser("123")
    .map { it.name.uppercase() }
    .flatMap { name -> IO.suspend { println("Found user: $name") } }

// Run the effect
program.runSync()
```

## Dependencies

### Internal

- `outcome` (implementation)

### External

- `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2` (implementation)
