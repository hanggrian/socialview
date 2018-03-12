import java.nio.file.Files.delete

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(android())
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath(dokka())
        classpath(gitPublish())
        classpath(bintrayRelease())
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/hendraanggrian/maven") // remove when pikasso is approved
    }
    tasks.withType<Javadoc> {
        isEnabled = false
    }
}

tasks {
    "clean"(Delete::class) {
        delete(buildDir)
    }
    "wrapper"(Wrapper::class) {
        gradleVersion = "4.4.1"
    }
}

/** bintray upload snippet
./gradlew bintrayUpload -PbintrayUser=hendraanggrian -PdryRun=false -PbintrayKey=
 */