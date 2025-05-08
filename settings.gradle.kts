pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}
dependencyResolutionManagement.repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
}

rootProject.name = "socialview"

include("socialview", "socialview-autocomplete")
include("sample")
include("website")
