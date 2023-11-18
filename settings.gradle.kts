pluginManagement.repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}
dependencyResolutionManagement.repositories {
    mavenCentral()
    google()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

rootProject.name = "socialview"

include("socialview", "socialview-autocomplete")
include("sample")
include("website")
