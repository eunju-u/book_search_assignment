plugins {
    `kotlin-dsl`
}

group = "com.example.booksearch.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // 플러그인 코드에서 AGP/Kotlin 타입(ApplicationExtension, KotlinCompile 등)을 쓰기 위해 컴파일 때만 사용
    // 실제 플러그인은 루트 build.gradle.kts의 apply false로 가져옴
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

// 플러그인 id와 구현 클래스 연결
gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "booksearch.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "booksearch.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "booksearch.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "booksearch.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = "booksearch.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
