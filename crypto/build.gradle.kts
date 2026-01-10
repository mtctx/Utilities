plugins {
    alias(libs.plugins.kotlin.jvm)
    id("mtctx.dokka-convention")
    id("mtctx.publish-convention")
}

group = "dev.mtctx.utilities"
version = "2.0.0"

base {
    archivesName = "crypto"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":bytearray"))
    implementation(project(":datasizes"))
    implementation(project(":outcome"))
    api(libs.bouncycastle.bcpkix)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}