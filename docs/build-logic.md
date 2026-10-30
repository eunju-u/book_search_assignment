# build-logic 사용 가이드

`build-logic`은 여러 모듈에서 반복하는 빌드 설정을 공통 플러그인으로 관리합니다. 이 플러그인을 **컨벤션 플러그인**이라고 합니다. 각 모듈은 필요한 플러그인을 적용하고, 기능별 의존성만 추가합니다.

- **공통 빌드 설정:** `build-logic`에서 관리합니다.
- **라이브러리·플러그인 버전:** `gradle/libs.versions.toml`에서 관리합니다.
- **모듈 전용 설정:** 각 모듈의 `build.gradle.kts`에서 관리합니다.

## 새 화면 모듈에 적용하기

루트 `settings.gradle.kts`에 모듈을 등록한 뒤, 해당 모듈의 `build.gradle.kts`를 아래처럼 작성합니다.

```kotlin
plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.feature)
}

android {
    namespace = "com.example.search"
}

dependencies {
    // 해당 기능에만 필요한 라이브러리를 추가합니다.
    implementation(libs.androidx.paging.compose)
}
```

**`feature` 플러그인에는 Android Library 설정이 포함되지 않으므로 `library`를 함께 적용해야 합니다.** 앱에서 화면을 사용하려면 앱의 모듈 의존성과 화면 경로도 연결해주세요.

## 플러그인별 역할

| 플러그인 ID | 적용 대상과 역할 |
| --- | --- |
| `booksearch.android.application` | 앱 모듈에 Android Application과 공통 SDK·컴파일·테스트 설정을 적용합니다. |
| `booksearch.android.library` | Android 라이브러리 모듈에 공통 SDK·컴파일·테스트 설정을 적용합니다. |
| `booksearch.jvm.library` | 순수 Kotlin 모듈에 Kotlin JVM, Java/Kotlin 타깃 17, JUnit을 적용합니다. |
| `booksearch.android.compose` | Compose 컴파일러, Compose BOM·UI·Material 3·개발 도구, Navigation Compose를 추가합니다. |
| `booksearch.android.hilt` | Hilt 플러그인과 런타임, KSP와 Hilt 컴파일러를 연결합니다. |
| `booksearch.android.feature` | Compose·Hilt와 화면 모듈의 공통 의존성을 묶어 적용합니다. |

### feature에 포함되는 의존성

| 구분 | 자동 적용 내용 |
| --- | --- |
| 컨벤션 플러그인 | `booksearch.android.compose`, `booksearch.android.hilt` |
| 공통 모듈 | `:core:designsystem`, `:core:ui`, `:core:domain` |
| 화면 라이브러리 | Navigation Compose, Lifecycle Runtime Compose, Hilt Navigation Compose |
| 단위 테스트 | JUnit, Coroutines Test |

Paging, Coil, Room 등 기능별 라이브러리는 필요한 모듈에서 직접 추가합니다. Kotlin Serialization 플러그인도 자동 적용되지 않으므로 `app`, `feature:detail`처럼 필요한 모듈에서 선언합니다.

### 공통 설정에서 알아둘 점

- Android 모듈의 `compileSdk`·`minSdk`는 버전 파일에서 읽고, Java/Kotlin 컴파일 타깃은 17로 맞춥니다.
- `targetSdk`는 앱에서는 `defaultConfig`, 라이브러리에서는 테스트와 Lint 설정에 적용합니다.
- Android 테스트에는 JUnit, AndroidX JUnit, Espresso와 `AndroidJUnitRunner`를 설정합니다.
- Compose에는 `ExperimentalMaterial3Api` opt-in을 적용하며, Tooling은 Debug 빌드에만 추가합니다.
- Hilt 플러그인은 Room 컴파일러를 추가하지 않습니다. `core:database`에서 Room의 KSP 의존성을 별도로 선언합니다.
- Gradle 실행 JDK는 컴파일 타깃과 별개로 `gradle/gradle-daemon-jvm.properties`에서 관리합니다.

현재 Navigation Compose는 Compose와 feature 양쪽에서, JUnit은 Android 공통 설정과 feature 양쪽에서 선언되어 있습니다. 또한 Compose 플러그인을 사용하는 `core:ui`와 `core:designsystem`에도 Navigation Compose가 포함됩니다.

## 현재 모듈별 적용 구성

아래 표는 플러그인 ID의 `booksearch.` 접두사를 생략했습니다.

| 모듈 | 직접 적용하는 컨벤션 플러그인 |
| --- | --- |
| `app` | `android.application`, `android.compose`, `android.hilt` |
| `feature:search`, `feature:favorite`, `feature:detail` | `android.library`, `android.feature` |
| `core:domain` | `jvm.library` |
| `core:data`, `core:network`, `core:database`, `core:common` | `android.library`, `android.hilt` |
| `core:designsystem`, `core:ui` | `android.library`, `android.compose` |

`app`과 `feature:detail`에는 외부 플러그인인 Kotlin Serialization도 적용합니다. 앱 ID·버전, namespace, BuildConfig, 모듈 간 의존성은 각 모듈에 남겨두었습니다. 순수 Kotlin 모듈인 `core:domain`에는 Android namespace가 없습니다.

## 설정을 수정할 위치

| 수정할 내용 | 파일 |
| --- | --- |
| 라이브러리·플러그인·SDK 버전과 별칭 | `gradle/libs.versions.toml` |
| Android SDK, Java/Kotlin 컴파일 타깃, 공통 테스트 설정 | `build-logic/convention/src/main/kotlin/com/example/booksearch/buildlogic/KotlinAndroid.kt` |
| Compose 공통 라이브러리와 컴파일 옵션 | 같은 디렉터리의 `AndroidCompose.kt` |
| 버전 카탈로그 접근 도우미 | 같은 디렉터리의 `ProjectExtensions.kt` |
| 플러그인별 적용 로직 | `build-logic/convention/src/main/kotlin/*ConventionPlugin.kt` |
| 컨벤션 플러그인 ID와 구현 클래스 연결 | `build-logic/convention/build.gradle.kts` |
| build-logic의 버전 카탈로그·저장소 연결 | `build-logic/settings.gradle.kts` |

새 컨벤션 플러그인을 추가하려면 구현 클래스를 작성하고, `convention/build.gradle.kts`에 등록한 뒤 `libs.versions.toml`에 별칭을 추가합니다.

## 루트 프로젝트와 연결되는 방식

루트 `settings.gradle.kts`의 `includeBuild("build-logic")`로 컨벤션 플러그인을 사용할 수 있게 합니다. `build-logic`은 별도 빌드이므로 자체 `settings.gradle.kts`에서 루트의 `libs.versions.toml`을 가져오고, 자체 `gradle.properties`에 병렬 빌드·캐시 설정을 둡니다.

`convention`은 `kotlin-dsl`을 사용하며 AGP·Kotlin Gradle 플러그인 타입을 `compileOnly`로 참조합니다. 실제 적용에 필요한 Android, Kotlin, KSP, Hilt 외부 플러그인은 루트 `build.gradle.kts`에 `apply false`로 선언합니다.

## 변경 후 확인 방법

API 키 등 사전 설정은 [README의 빌드 방법](../README.md#빌드-방법)을 참고해주세요. 프로젝트 루트에서 실행하며, Windows에서는 `bash gradlew` 대신 `.\gradlew.bat`을 사용합니다.

```bash
# 컨벤션 플러그인 빌드
bash gradlew -p build-logic :convention:build

# 실제 앱 모듈에 적용되는지 확인
bash gradlew :app:assembleDebug --warning-mode all

# Android 모듈 및 순수 Kotlin 모듈의 단위 테스트
bash gradlew testDebugUnitTest :core:domain:test

# 화면 모듈의 최종 의존성 확인
bash gradlew :feature:search:dependencies --configuration debugRuntimeClasspath
```

컨벤션 플러그인 빌드만으로는 앱에 적용된 결과까지 확인할 수 없으므로 앱 빌드도 함께 확인해주세요.
