@file:Suppress("UNUSED_VARIABLE")

import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.language.base.plugins.LifecycleBasePlugin

fun Dependencies.ktlint(module: String? = null) = when (module) {
    null -> "com.pinterest:ktlint:0.41.0"
    else -> "com.pinterest.ktlint:ktlint-$module:0.41.0"
}

fun Project.ktlint(vararg rulesets: Any, src: String = "src") {
    val configuration = configurations.register("ktlint")
    dependencies {
        configuration {
            invoke(ktlint())
            rulesets.forEach { invoke(it) }
        }
    }
    tasks {
        val ktlint by registering(JavaExec::class) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            inputs.dir(src)
            outputs.dir(src)
            description = "Check Kotlin code style."
            classpath(configuration)
            main = "com.pinterest.ktlint.Main"
            args("$src/**/*.kt")
        }
        val check by getting {
            dependsOn(ktlint)
        }
        val ktlintFormat by registering(JavaExec::class) {
            group = "formatting"
            inputs.dir(src)
            outputs.dir(src)
            description = "Fix Kotlin code style deviations."
            classpath(configuration)
            main = "com.pinterest.ktlint.Main"
            args("-F", "$src/**/*.kt")
        }
    }
}