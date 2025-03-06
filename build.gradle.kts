import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishBasePlugin
import com.vanniktech.maven.publish.SonatypeHost

val developerId: String by project
val developerName: String by project
val developerUrl: String by project
val releaseGroup: String by project
val releaseArtifact: String by project
val releaseVersion: String by project
val releaseDescription: String by project
val releaseUrl: String by project

val jdkVersion = JavaLanguageVersion.of(libs.versions.jdk.get())
val jreVersion = JavaLanguageVersion.of(libs.versions.jre.get())

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.maven.publish) apply false
}

allprojects {
    group = releaseGroup
    version = releaseVersion
}

subprojects {
    plugins.withType<LibraryPlugin>().configureEach {
        modify(the<LibraryExtension>())
    }
    plugins.withType<AppPlugin>().configureEach {
        modify(the<BaseAppModuleExtension>())
    }
    plugins.withType<CheckstylePlugin>().configureEach {
        the<CheckstyleExtension>().toolVersion = libs.versions.checkstyle.get()
        tasks.register<Checkstyle>("checkstyleAndroid") {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            description = "Generate Android lint report"

            source("src")
            include("**/*.java")
            exclude("**/gen/**", "**/R.java")
            classpath = files()
        }
    }
    plugins.withType<JacocoPlugin>().configureEach {
        tasks {
            withType<Test>().configureEach {
                configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
            register<JacocoReport>("jacocoAndroid") {
                group = "Reporting"
                description = "Generate Android test coverage"

                dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")
                mustRunAfter("test")
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }
                sourceDirectories.setFrom(layout.projectDirectory.dir("src/main/java"))
                classDirectories.setFrom(
                    files(
                        fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                            exclude(
                                "**/R.class",
                                "**/R\$*.class",
                                "**/BuildConfig.*",
                                "**/Manifest*.*",
                                "**/*Test*.*",
                                "**/*Args.*",
                                "**/*Directions.*",
                            )
                        },
                    ),
                )
                executionData.setFrom(
                    files(
                        fileTree(layout.buildDirectory) {
                            include("**/*.exec", "**/*.ec")
                        }
                    ),
                )
            }
        }
    }
    plugins.withType<MavenPublishBasePlugin> {
        configure<MavenPublishBaseExtension> {
            configure(AndroidSingleVariantLibrary())
            publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
            signAllPublications()
            pom {
                name.set(project.name)
                description.set(releaseDescription)
                url.set(releaseUrl)
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                        url.set(developerUrl)
                    }
                }
                scm {
                    url.set(releaseUrl)
                    connection.set("scm:git:https://github.com/$developerId/$releaseArtifact.git")
                    developerConnection
                        .set("scm:git:ssh://git@github.com/$developerId/$releaseArtifact.git")
                }
            }
        }
    }
}

fun modify(extension: BaseExtension) {
    extension.setCompileSdkVersion(libs.versions.sdk.target.get().toInt())
    extension.defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        version = releaseVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    extension.compileOptions {
        sourceCompatibility = JavaVersion.toVersion(jreVersion)
        targetCompatibility = JavaVersion.toVersion(jreVersion)
    }
}
