import org.gradle.api.artifacts.dsl.DependencyHandler

const val bintrayUser = "hendraanggrian"
const val bintrayGroup = "com.hendraanggrian"
const val bintrayArtifact = "socialview"
const val bintrayPublish = "0.17"
const val bintrayDesc = "Android TextView and EditText with hashtag, mention, and hyperlink support"
const val bintrayWeb = "https://github.com/hendraanggrian/socialview"

const val minSdk = 14
const val targetSdk = 27
const val buildTools = "27.0.1"

const val kotlinVersion = "1.1.61"
const val kotaVersion = "0.21"
const val supportVersion = "27.0.1"
const val picassoUtilsVersion = "0.3"

const val junitVersion = "4.12"
const val runnerVersion = "1.0.1"
const val espressoVersion = "3.0.1"

fun DependencyHandler.support(module: String, version: String, vararg suffixes: String) = "${StringBuilder("com.android.support").apply { suffixes.forEach { append(".$it") } }}:$module:$version"
fun DependencyHandler.hendraanggrian(module: String, version: String) = "com.hendraanggrian:$module:$version"
fun DependencyHandler.junit(version: String) = "junit:junit:$version"