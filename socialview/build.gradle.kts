plugins {
    android("library")
    kotlin("android")
    `maven-publish`
    signing
}

android {
    compileSdkVersion(SDK_TARGET)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        multiDexEnabled = true
        versionName = RELEASE_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    sourceSets {
        named("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDir("src")
            res.srcDir("res")
            resources.srcDir("src")
        }
        named("androidTest") {
            setRoot("tests")
            manifest.srcFile("tests/AndroidManifest.xml")
            java.srcDir("tests/src")
            res.srcDir("tests/res")
            resources.srcDir("tests/src")
        }
    }
    libraryVariants.all {
        generateBuildConfigProvider.orNull?.enabled = false
    }
}

dependencies {
    implementation(androidx("appcompat"))
    implementation(androidx("annotation", version = "1.2.0"))
    androidTestImplementation(kotlin("stdlib", VERSION_KOTLIN))
    androidTestImplementation(kotlin("test-junit", VERSION_KOTLIN))
    androidTestImplementation(material())
    androidTestImplementation(androidx("multidex", version = VERSION_MULTIDEX))
    androidTestImplementation(androidx("test", "core-ktx", VERSION_ANDROIDX_TEST))
    androidTestImplementation(androidx("test", "runner", VERSION_ANDROIDX_TEST))
    androidTestImplementation(androidx("test", "rules", VERSION_ANDROIDX_TEST))
    androidTestImplementation(androidx("test.ext", "junit-ktx", VERSION_ANDROIDX_JUNIT))
    androidTestImplementation(androidx("test.ext", "truth", VERSION_ANDROIDX_TRUTH))
    androidTestImplementation(androidx("test.espresso", "espresso-core", VERSION_ESPRESSO))
}

tasks {
    val javadoc by registering(Javadoc::class) {
        isFailOnError = false
        source = android.sourceSets["main"].java.getSourceFiles()
        classpath += project.files(android.bootClasspath.joinToString(File.pathSeparator))
        classpath += configurations.compile
    }
}

mavenPublishAndroid(
    artifact = RELEASE_ARTIFACT,
    sources = android.sourceSets["main"].java.srcDirs
)