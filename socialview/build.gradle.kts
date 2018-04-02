import org.gradle.kotlin.dsl.kotlin

plugins {
    `git-publish`
}

gitPublish {
    repoUri = RELEASE_WEBSITE
    branch = "gh-pages"
    contents.from(
        "pages",
        "../socialview-core/build/docs",
        "../socialview-commons/build/docs")
}

tasks["gitPublishCopy"].dependsOn(
    ":socialview-core:dokka",
    ":socialview-commons:dokka")