const val SDK_MIN = 14
const val SDK_TARGET = 29

private const val VERSION_ANDROID_PLUGIN = "3.6.3"
const val VERSION_MULTIDEX = "2.0.1"
const val VERSION_ANDROIDX = "1.1.0"
const val VERSION_ANDROIDX_TEST = "1.2.0"
const val VERSION_ANDROIDX_JUNIT = "1.1.1"
const val VERSION_ANDROIDX_TRUTH = "1.2.0"
const val VERSION_ESPRESSO = "3.2.0"

fun Dependencies.android() = "com.android.tools.build:gradle:$VERSION_ANDROID_PLUGIN"

fun Plugins.android(submodule: String) = id("com.android.$submodule")

fun Dependencies.androidx(repository: String, module: String = repository, version: String = VERSION_ANDROIDX) =
    "androidx.$repository:$module:$version"

fun Dependencies.material(version: String = VERSION_ANDROIDX) = "com.google.android.material:material:$version"
