buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(android)
        classpath(`git-publish`)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}