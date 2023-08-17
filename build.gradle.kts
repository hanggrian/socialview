import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    kotlin("android") version libs.versions.kotlin apply false
}

allprojects {
    group = RELEASE_GROUP
    version = RELEASE_VERSION
}

subprojects {
    plugins.withType<LibraryPlugin>().configureEach {
        configure<LibraryExtension>(::configureAndroid)
    }
    plugins.withType<AppPlugin>().configureEach {
        configure<BaseAppModuleExtension>(::configureAndroid)
    }
    plugins.withType<KotlinAndroidPluginWrapper> {
        kotlinExtension.jvmToolchain(libs.versions.jdk.get().toInt())
        (the<BaseExtension>() as ExtensionAware).extensions.getByType<KotlinJvmOptions>()
            .jvmTarget = JavaVersion.toVersion(libs.versions.jdk.get()).toString()
    }
}

fun configureAndroid(extension: BaseExtension) {
    extension.setCompileSdkVersion(libs.versions.sdk.target.get().toInt())
    extension.defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        version = RELEASE_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    extension.compileOptions {
        targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get())
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get())
    }
}
