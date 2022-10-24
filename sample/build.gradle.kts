plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.android.extensions)
    alias(libs.plugins.kotlin.kapt)
}

android {
    defaultConfig {
        minSdk = 23
        applicationId = "com.example.socialview"
        multiDexEnabled = true
    }
    lint.abortOnError = false
}

dependencies {
    implementation(project(":$RELEASE_ARTIFACT-commons"))
    implementation(project(":$RELEASE_ARTIFACT"))
    implementation(libs.material)
    implementation(libs.androidx.multidex)
    implementation(libs.process.phoenix)
}
