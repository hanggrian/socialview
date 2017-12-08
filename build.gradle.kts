buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.novoda:bintray-release:0.7.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
    tasks.withType(Javadoc::class.java).all {
        isEnabled = false
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

/** QUICK LINT CHECK BEFORE UPLOAD
./gradlew socialview:bintrayUpload -PdryRun=false -PbintrayUser=hendraanggrian -PbintrayKey=
./gradlew socialview-commons:bintrayUpload -PdryRun=false -PbintrayUser=hendraanggrian -PbintrayKey=
 */