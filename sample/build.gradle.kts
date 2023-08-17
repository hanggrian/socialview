plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version libs.versions.kotlin
}

android {
    namespace = "com.example.$RELEASE_ARTIFACT"
    testNamespace = "$namespace.test"
    defaultConfig {
        applicationId = "com.example.$RELEASE_ARTIFACT"
        multiDexEnabled = true
    }
    lint.abortOnError = false
}

dependencies {
    implementation(project(":$RELEASE_ARTIFACT-autocomplete"))
    implementation(libs.material)
    implementation(libs.androidx.multidex)
    implementation(libs.process.phoenix)
}
