val releaseGroup: String by project
val releaseArtifact: String by project

plugins {
    alias(libs.plugins.android.library)
    checkstyle
    jacoco
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "$releaseGroup.$releaseArtifact.autocomplete"
    testNamespace = "$namespace.test"
    buildFeatures.buildConfig = false
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    checkstyle(libs.rulebook.checkstyle)

    api(project(":$releaseArtifact"))
    implementation(libs.androidx.appcompat)
    implementation(libs.picasso)

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
}
