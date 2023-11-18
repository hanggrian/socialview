val releaseArtifact: String by project

plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version libs.versions.kotlin
}

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
