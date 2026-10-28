dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    // 루트 프로젝트의 버전 카탈로그를 build-logic에서도 libs로 사용
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
