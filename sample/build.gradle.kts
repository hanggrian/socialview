import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val releaseArtifact: String by project

val jdkVersion = JavaLanguageVersion.of(libs.versions.jdk.get())
val jreVersion = JavaLanguageVersion.of(libs.versions.jre.get())

plugins {
    kotlin("android") version libs.versions.kotlin.get()
    alias(libs.plugins.android.application)
}

kotlin.jvmToolchain(jdkVersion.asInt())

android {
    namespace = "com.example"
    defaultConfig {
        applicationId = "com.example"
        multiDexEnabled = true
    }
    lint.abortOnError = false
}

dependencies {
    implementation(project(":$releaseArtifact-autocomplete"))
    implementation(libs.material)
    implementation(libs.androidx.multidex)
    implementation(libs.process.phoenix)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget
        .set(JvmTarget.fromTarget(JavaVersion.toVersion(jreVersion).toString()))
}
