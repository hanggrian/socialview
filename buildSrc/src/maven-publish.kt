import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension

const val REPOSITORIES_OSSRH_RELEASES = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
const val REPOSITORIES_OSSRH_SNAPSHOTS = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

fun Project.mavenPublishJvm(
    artifact: String = RELEASE_ARTIFACT,
    javadoc: Any = defaultJavadoc,
    sources: Any = defaultSources
) = mavenPublish(artifact, javadoc, sources, "java")

fun Project.mavenPublishAndroid(
    artifact: String = RELEASE_ARTIFACT,
    javadoc: Any = defaultJavadoc,
    sources: Any
) = afterEvaluate { mavenPublish(artifact, javadoc, sources, "release") }

private val Project.defaultJavadoc: Any get() = tasks.findByName("dokkaJavadoc") ?: tasks["javadoc"]
private val Project.defaultSources: Any get() = (extensions["sourceSets"] as SourceSetContainer)["main"].allSource

private fun isSnapshot() = RELEASE_VERSION.endsWith("SNAPSHOT")

private fun Project.mavenPublish(artifact: String, javadoc: Any, sources: Any, component: String) {
    checkNotNull(plugins.findPlugin("maven-publish")) { "Missing plugin `maven-publish` for this publication" }
    checkNotNull(plugins.findPlugin("signing")) { "Missing plugin `signing` for this publication" }
    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(javadoc)
        if (javadoc is Task || javadoc is TaskProvider<*>) {
            dependsOn(javadoc)
        }
    }
    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sources)
    }
    val publishing = extensions.getByType<PublishingExtension>()
    publishing.repositories.maven {
        url = `java.net`.URI(if (isSnapshot()) REPOSITORIES_OSSRH_SNAPSHOTS else REPOSITORIES_OSSRH_RELEASES)
        credentials {
            username = checkNotNull(System.getenv("OSSRH_USERNAME")) {
                "Missing environment `OSSRH_USERNAME` for this publication"
            }
            password = checkNotNull(System.getenv("OSSRH_PASSWORD")) {
                "Missing environment `OSSRH_PASSWORD` for this publication"
            }
        }
    }
    val mavenJava by publishing.publications.registering(MavenPublication::class) {
        groupId = RELEASE_GROUP
        artifactId = artifact
        version = RELEASE_VERSION
        from(components[component])
        artifact(javadocJar)
        artifact(sourcesJar)
        pom {
            name.set(artifact)
            description.set(RELEASE_DESCRIPTION)
            url.set(RELEASE_GITHUB)
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("hendraanggrian")
                    name.set("Hendra Anggrian")
                    email.set("hendraanggrian@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:$RELEASE_GITHUB.git")
                developerConnection.set("scm:git:ssh:$RELEASE_GITHUB.git")
                url.set(RELEASE_GITHUB)
            }
        }
    }
    extensions.getByType<SigningExtension>().sign(mavenJava.get())
    tasks.withType<Sign> { onlyIf { isSnapshot() } }
}