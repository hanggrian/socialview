import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    checkstyle
    jacoco
    alias(libs.plugins.maven.publish)
}

android {
    buildFeatures.buildConfig = false
    testOptions.unitTests.isIncludeAndroidResources = true
}

checkstyle {
    configFile = projectDir.resolve("rulebook_checks.xml")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)
    signAllPublications()
    pom(::pom)
    configure(AndroidSingleVariantLibrary())
}

dependencies {
    checkstyle(libs.rulebook.checkstyle)
    implementation(libs.material)
    testImplementation(libs.bundles.androidx.test)
}

tasks {
    withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }
    register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
        val fileFilter = listOf(
            "**/R.class", "**/R\$*.class", "**/BuildConfig.*",
            "**/Manifest*.*", "**/*Test*.*", "android/**/*.*"
        )
        val debugTree =
            fileTree("dir" to "$buildDir/intermediates/javac/debug", "excludes" to fileFilter)
        val mainSrc = "$projectDir/src/main/java"
        sourceDirectories.setFrom(mainSrc)
        classDirectories.setFrom(debugTree)
        executionData.setFrom(
            fileTree(
                "dir" to buildDir,
                "includes" to listOf("jacoco/testDebugUnitTest.exec")
            )
        )
    }

    val checkstyle by registering(Checkstyle::class) {
        source("src/main/java", "src/test/java")
        include("**/*.java")
        classpath = files()
    }
    check.dependsOn(checkstyle)
}
