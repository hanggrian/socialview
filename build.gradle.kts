import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    dependencies.classpath(libs.android)
}

plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.android.extensions) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

allprojects {
    group = RELEASE_GROUP
    version = RELEASE_VERSION
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    plugins.withType<LibraryPlugin>().configureEach {
        configure<LibraryExtension>(::androidConfig)
    }
    plugins.withType<AppPlugin>().configureEach {
        configure<BaseAppModuleExtension>(::androidConfig)
    }
    plugins.withType<KotlinAndroidPluginWrapper> {
        kotlinExtension.jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
        }
        (the<BaseExtension>() as ExtensionAware).extensions.getByType<KotlinJvmOptions>()
            .jvmTarget = JavaVersion.toVersion(libs.versions.jdkAndroid.get()).toString()
    }
}

fun androidConfig(extension: BaseExtension) {
    extension.setCompileSdkVersion(libs.versions.sdkTarget.get().toInt())
    extension.defaultConfig {
        minSdk = libs.versions.sdkMin.get().toInt()
        targetSdk = libs.versions.sdkTarget.get().toInt()
        version = RELEASE_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    extension.compileOptions {
        targetCompatibility = JavaVersion.toVersion(libs.versions.jdkAndroid.get())
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jdkAndroid.get())
    }
}
