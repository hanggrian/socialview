internal typealias Plugins = org.gradle.plugin.use.PluginDependenciesSpec
internal typealias Dependencies = org.gradle.api.artifacts.dsl.DependencyHandler

const val VERSION_MULTIDEX = "2.0.1"
const val VERSION_ANDROIDX = "1.3.0"
const val VERSION_ANDROIDX_TEST = "1.4.0"
const val VERSION_ANDROIDX_JUNIT = "1.1.3"
const val VERSION_ANDROIDX_TRUTH = "1.4.0"
const val VERSION_ESPRESSO = "3.4.0"
val Dependencies.android get() = "com.android.tools.build:gradle:7.0.3"
fun Plugins.android(submodule: String) = id("com.android.$submodule")
fun Dependencies.material(version: String = VERSION_ANDROIDX) = "com.google.android.material:material:$version"
fun Dependencies.androidx(repository: String, module: String = repository, version: String = VERSION_ANDROIDX) =
    "androidx.$repository:$module:$version"

const val VERSION_KOTLIN = "1.5.31"
const val VERSION_COROUTINES = "1.5.2"
val Dependencies.dokka get() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_KOTLIN"
val Plugins.dokka get() = id("org.jetbrains.dokka")
fun Dependencies.kotlinx(module: String, version: String? = null) =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$it" }.orEmpty()}"

val Dependencies.`git-publish` get() = "org.ajoberstar:gradle-git-publish:3.0.0"
val Plugins.`git-publish` get() = id("org.ajoberstar.git-publish")

fun Dependencies.picasso() = "com.squareup.picasso:picasso:2.8"
