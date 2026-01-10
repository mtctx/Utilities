plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokka.javadoc)
}

repositories {
    mavenCentral()
}

dependencies {
    subprojects.forEach { subproject ->
        dokka(subproject)
    }
}

dokka {
    moduleName.set("mtctx's Utilities")

    dokkaPublications.html {
        outputDirectory.set(layout.projectDirectory.dir("dokka/html"))
    }
    dokkaPublications.javadoc {
        outputDirectory.set(layout.projectDirectory.dir("dokka/javadoc"))
    }
}

tasks {
    clean {
        doLast {
            delete(layout.projectDirectory.dir("dokka"))
        }
    }
}