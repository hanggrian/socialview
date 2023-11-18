val developerId: String by project
val developerName: String by project
val developerUrl: String by project
val releaseArtifact: String by project
val releaseDescription: String by project
val releaseUrl: String by project

plugins {
    alias(libs.plugins.pages)
    alias(libs.plugins.git.publish)
}

pages {
    contents.index(rootDir.resolve("README.md"))
    styles.add("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-tomorrow.min.css")
    scripts.addAll(
        "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/prism.min.js",
        "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-groovy.min.js",
        "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-java.min.js",
    )
    cayman {
        darkTheme()
        authorName = developerName
        authorUrl = developerUrl
        projectName = releaseArtifact
        projectDescription = releaseDescription
        projectUrl = releaseUrl
    }
}

gitPublish {
    repoUri.set("git@github.com:$developerId/$releaseArtifact.git")
    branch.set("gh-pages")
    contents.from(pages.outputDirectory)
}

tasks {
    register(LifecycleBasePlugin.CLEAN_TASK_NAME) {
        delete(layout.buildDirectory)
    }
}
