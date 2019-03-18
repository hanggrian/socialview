import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec

fun DependencyHandler.android() = "com.android.tools.build:gradle:$VERSION_ANDROID_PLUGIN"
fun PluginDependenciesSpec.android(submodule: String) = id("com.android.$submodule")

fun DependencyHandler.androidx(
    repository: String,
    module: String = repository,
    version: String = VERSION_ANDROIDX
): String = "androidx.$repository:$module:$version"

fun DependencyHandler.material() = "com.google.android.material:material:$VERSION_ANDROIDX"

fun DependencyHandler.picasso(): String = "com.squareup.picasso:picasso:2.71828"

fun DependencyHandler.bintray() = "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4"
inline val PluginDependenciesSpec.bintray get() = id("com.jfrog.bintray")

fun DependencyHandler.bintrayRelease() = "com.novoda:bintray-release:0.9"
inline val PluginDependenciesSpec.`bintray-release` get() = id("com.novoda.bintray-release")