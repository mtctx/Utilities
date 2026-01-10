plugins {
    id("base")
    id("org.jetbrains.dokka")
    id("org.jetbrains.dokka-javadoc")
}

val projectName = base.archivesName.get()!!

dokka {
    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka/html/$projectName").get().asFile)
    }
    dokkaPublications.javadoc {
        outputDirectory.set(layout.buildDirectory.dir("dokka/javadoc/$projectName").get().asFile)
    }

    dokkaSourceSets.configureEach {
        jdkVersion.set(21)
        sourceLink {
            localDirectory.set(file("$projectName/src/main/kotlin"))
            remoteUrl.set(uri("https://github.com/mtctx/Utilities/tree/main/$projectName/src/main/kotlin/"))
            remoteLineSuffix.set("#L")
        }
    }
}