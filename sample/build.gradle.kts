import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val releaseArtifact: String by project

val jdkVersion = JavaLanguageVersion.of(libs.versions.jdk.get())
val jreVersion = JavaLanguageVersion.of(libs.versions.jre.get())

plugins {
    kotlin("android") version libs.versions.kotlin.get()
    alias(libs.plugins.android.application)
    alias(libs.plugins.ktlint)
}

kotlin.jvmToolchain(jdkVersion.asInt())

ktlint.version.set(libs.versions.ktlint.get())

android {
    namespace = "com.example"
    defaultConfig {
        applicationId = "com.example"
        multiDexEnabled = true
    }
}

dependencies {
    ktlintRuleset(libs.rulebook.ktlint)

    implementation(project(":$releaseArtifact-autocomplete"))
    implementation(libs.material)
    implementation(libs.androidx.multidex)
    implementation(libs.picasso)
    implementation(libs.process.phoenix)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions
        .jvmTarget
        .set(JvmTarget.fromTarget(JavaVersion.toVersion(jreVersion).toString()))
}
