import org.gradle.kotlin.dsl.kotlin

plugins {
    `git-publish`
}

gitPublish {
    repoUri = RELEASE_WEBSITE
    branch = "gh-pages"
    contents.from(
        "src",
        "../$RELEASE_ARTIFACT-core/build/docs",
        "../$RELEASE_ARTIFACT-commons/build/docs")
}

tasks["gitPublishCopy"].dependsOn(
    ":$RELEASE_ARTIFACT-core:dokka",
    ":$RELEASE_ARTIFACT-commons:dokka")