pluginManagement {
    // build-logic의 컨벤션 플러그인(booksearch.*)을 모듈에서 쓸 수 있게 포함
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BookSearch"
include(":app")
include(":core:data")
include(":core:database")
include(":core:designsystem")
include(":core:domain")
include(":core:network")
include(":feature:detail")
include(":feature:favorite")
include(":feature:search")
