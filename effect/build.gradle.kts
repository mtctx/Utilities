plugins {
    alias(libs.plugins.kotlin.jvm)
    id("mtctx.dokka-convention")
    id("mtctx.publish-convention")
}

base {
    archivesName = "effect"
}

group = "dev.mtctx.utilities"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":outcome"))
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}