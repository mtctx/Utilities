plugins {
    alias(libs.plugins.kotlin.jvm)
    id("mtctx.dokka-convention")
    id("mtctx.publish-convention")
}

base {
    archivesName = "io"
}

group = "dev.mtctx.utilities"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":serialization"))
    implementation(project(":outcome"))
    implementation(libs.kotlinx.serialization.json)
    api(libs.okio)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}