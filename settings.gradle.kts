plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "Utilities"

includeBuild("gradle/plugins")

include(":crypto")
include(":datasizes")
include(":effect")
include(":serialization")
include(":outcome")
include(":ignore")
include(":io")
include(":bytearray")
include(":ktor-client")