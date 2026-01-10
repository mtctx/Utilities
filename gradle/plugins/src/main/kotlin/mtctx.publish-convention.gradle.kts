import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    id("base")
    id("com.vanniktech.maven.publish")
    signing
}

val artifactId = base.archivesName.get()!!

afterEvaluate {
    mavenPublishing {
        coordinates("dev.mtctx.library", "utilities-$artifactId", version.toString())
    }
}

mavenPublishing {
    pom {
        name.set("mtctx utilities")
        description.set("Things I often use in my software, e.g. Outcome")
        inceptionYear.set("2025")
        url.set("https://github.com/mtctx/Utilities")

        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                distribution.set("repo")
            }
        }

        scm {
            url.set("https://github.com/mtctx/Utils")
            connection.set("scm:git:git@github.com:mtctx/Utils.git")
            developerConnection.set("scm:git:ssh://git@github.com:mtctx/Utils.git")
        }

        developers {
            developer {
                id.set("mtctx")
                name.set("mtctx")
                email.set("me@mtctx.dev")
            }
        }
    }

    configure(KotlinJvm(JavadocJar.Dokka("dokkaGenerateJavadoc"), sourcesJar = true))

    signAllPublications()
    publishToMavenCentral(automaticRelease = true)
}

signing {
    useGpgCmd()
}