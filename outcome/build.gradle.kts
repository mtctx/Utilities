plugins {
    alias(libs.plugins.kotlin.jvm)
    id("mtctx.dokka-convention")
    id("mtctx.publish-convention")
}

base {
    archivesName = "outcome"
}

group = "dev.mtctx.utilities"
version = "2.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}