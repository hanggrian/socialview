buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
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