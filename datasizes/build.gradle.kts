plugins {
    alias(libs.plugins.kotlin.jvm)
    id("mtctx.dokka-convention")
    id("mtctx.publish-convention")
}

group = "dev.mtctx.utilities"
version = "2.0.0"

base {
    archivesName = "datasizes"
}

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