# crypto module

The `crypto` module provides high-level cryptographic utilities for secure data handling. It includes robust
implementations for password hashing using Argon2, message authentication via HMAC-SHA256, and other common
cryptographic operations, abstracting the complexity of underlying security libraries while maintaining best practices.

## Installation

<details>
<summary>Gradle (Kotlin)</summary>

```kotlin
implementation("dev.mtctx.library:utilities-crypto:2.0.0")
```

</details>

<details>
<summary>Gradle (Groovy)</summary>

```groovy
implementation 'dev.mtctx.library:utilities-crypto:2.0.0'
```

</details>

<details>
<summary>Maven</summary>

```xml

<dependency>
    <groupId>dev.mtctx.library</groupId>
    <artifactId>utilities-crypto</artifactId>
    <version>2.0.0</version>
</dependency>
```

</details>

<details>
<summary>Ivy</summary>

```xml

<dependency org="dev.mtctx.library" name="utilities-crypto" rev="2.0.0"/>
```

</details>

<details>
<summary>sbt</summary>

```scala
libraryDependencies += "dev.mtctx.library" % "utilities-crypto" % "2.0.0"
```

</details>

<details>
<summary>Leiningen</summary>

```clojure
[dev.mtctx.library/utilities-crypto "2.0.0"]
```

</details>

## Usage Examples

### Password Hashing with Argon2

Securely hash and verify passwords using the Argon2 algorithm.

```kotlin
val password = "mySecurePassword"
val hash = Argon2.hash(password)

// Verification
val isValid = Argon2.verify(password, hash)
```

### HMAC-SHA256 Authentication

Generate and verify HMAC tags to ensure data integrity.

```kotlin
val data = "some important data"
val hmacResult = HmacSha256.generate(data)
val tag = hmacResult.tag
val key = hmacResult.key

// Verify later
val isAuthentic = HmacSha256.verify(data, tag, key)
```

## Dependencies

### Internal

- `bytearray` (implementation)
- `datasizes` (implementation)
- `outcome` (implementation)

### External

- `org.bouncycastle:bcpkix-jdk18on:1.83` (api - exposed)
