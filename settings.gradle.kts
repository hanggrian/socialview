pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}
dependencyResolutionManagement.repositories {
    mavenCentral()
    google()
}

rootProject.name = "socialview"

include("socialview", "socialview-autocomplete")
include("sample")
include("website")
