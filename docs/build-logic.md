# build-logic 작업 기록

## 1. 목적

모듈마다 `build.gradle.kts`에 반복되던 빌드 설정(SDK 버전, JDK, Compose, Hilt 등)을
`build-logic`의 컨벤션 플러그인으로 모아 한곳에서 관리한다.
각 모듈은 필요한 플러그인만 골라 적용하고, 그 모듈에만 필요한 설정과 의존성만 적는다.

## 2. 추가한 build-logic 파일

```
build-logic/
├─ settings.gradle.kts        루트의 gradle/libs.versions.toml을 libs로 연결, :convention 포함
├─ gradle.properties          병렬 빌드, 빌드 캐시 (루트 설정은 included build에 전달되지 않음)
├─ .gitignore
└─ convention/
   ├─ build.gradle.kts        kotlin-dsl, compileOnly(AGP, Kotlin Gradle 플러그인), 플러그인 등록
   └─ src/main/kotlin/
      ├─ AndroidApplicationConventionPlugin.kt
      ├─ AndroidLibraryConventionPlugin.kt
      ├─ AndroidComposeConventionPlugin.kt
      ├─ AndroidHiltConventionPlugin.kt
      ├─ JvmLibraryConventionPlugin.kt
      └─ com/example/booksearch/buildlogic/
         ├─ ProjectExtensions.kt  libs 단축 속성, toml 버전을 숫자로 읽는 findVersionInt()
         ├─ KotlinAndroid.kt      Android 모듈 공통(SDK, JDK 17, 테스트 의존성), 순수 Kotlin 모듈 공통(JDK 17)
         └─ AndroidCompose.kt     Compose 켜기 + Compose 기본 라이브러리 + Material3 opt-in
```

### 플러그인별 역할

| 플러그인 id | 언제 쓰나 | 해 주는 것 |
|---|---|---|
| `booksearch.android.application` | 앱 모듈 | 앱 기본 설정 (SDK 버전, JDK 17, 테스트 라이브러리) |
| `booksearch.android.library` | Android 라이브러리 모듈 | 라이브러리 기본 설정 (SDK 버전, JDK 17, 테스트 라이브러리) |
| `booksearch.android.compose` | Compose 화면을 그리는 모듈 | Compose 켜기 + Compose 기본 라이브러리 |
| `booksearch.android.hilt` | Hilt를 쓰는 모듈 | Hilt 사용 준비 (Hilt 플러그인, 라이브러리, KSP) |
| `booksearch.jvm.library` | Android 없이 Kotlin만 쓰는 모듈 | 순수 Kotlin 기본 설정 (JDK 17, JUnit) |

- application·library·jvm.library 중 하나를 기본으로 적용하고, 필요하면 compose·hilt를 더한다.
- 구현 클래스는 `Android…ConventionPlugin` / `JvmLibraryConventionPlugin` (id 순서와 같음).

## 3. 루트에 추가한 것

- `settings.gradle.kts` : `pluginManagement { includeBuild("build-logic") }`
- `build.gradle.kts` : build-logic이 안에서 적용하는 플러그인을 `apply false`로 추가 (`android.library`, `kotlin.jvm`, `hilt`),
  모듈에서 타입 안전 내비게이션 경로에 쓸 수 있게 `kotlin.serialization`도 추가
- `gradle/libs.versions.toml`
  - `[versions]` : `hilt`, `hiltNavigationCompose`, `navigation`, `lifecycle`, `coroutines`, `javaxInject`, `compileSdk`, `minSdk`, `targetSdk`
  - `[libraries]` : `hilt-android`, `hilt-compiler`, `androidx-hilt-navigation-compose`, `androidx-navigation-compose`,
    `androidx-lifecycle-runtime-compose`, `kotlinx-coroutines-core`,
    `javax-inject`,
    `android-gradlePlugin`, `kotlin-gradlePlugin` (build-logic 컴파일용)
  - `[plugins]` : `android-library`, `kotlin-jvm`, `kotlin-serialization`, `hilt`, `booksearch-*` 5개

## 4. 모듈별로 바꾼 것

모듈에는 **기본 플러그인 + `namespace`** 만 두었다. Hilt, Compose, 모듈 간 의존은 모듈을 구현할 때 정한다.

| 모듈 | 플러그인 | 모듈에 남긴 것 |
|---|---|---|
| `app` | application, compose | namespace, applicationId, versionCode/Name, release `optimization`, 기존 의존성 |
| `core:*`, `feature:*` | library | namespace |

**모듈에서 빠지고 build-logic이 대신 넣는 것**

- `compileSdk`, `minSdk`, `targetSdk`
- `compileOptions` (Java 17), `testInstrumentationRunner`
- 테스트 의존성 : JUnit, AndroidX Test, Espresso
- `app`의 Compose 설정 : `buildFeatures.compose`, Compose BOM·UI·Material3·preview

**모듈에서 지운 것** (`core:*`, `feature:*`, 앱 모듈 → 라이브러리 모듈로 바꾸면서)

- `applicationId`, `versionCode`, `versionName`, release `optimization`
- appcompat, material, core-ktx 의존성 (템플릿 기본값)

## 5. 정리한 파일

`core:*`, `feature:*`에 앱 모듈 템플릿으로 생성되어 있던 파일.
appcompat·material을 뺀 뒤에도 남아 있으면 리소스를 찾지 못해 빌드가 실패한다.

| 지운 파일 | 내용 |
|---|---|
| `src/main/res` (아이콘, 테마, 색) | mipmap, drawable, `Theme.MaterialComponents` 테마, colors |
| `src/main/AndroidManifest.xml` | 아이콘·테마를 참조하는 `<application>` 태그만 있던 파일 (라이브러리 모듈은 manifest가 없어도 됨) |

`feature:*`의 `res/values/strings.xml`은 남아 있다.


## 6. 검증 방법

빌드 (설정 오타, toml에 없는 이름, 빠진 `apply false`는 여기서 실패)

```bash
./gradlew clean assembleDebug --warning-mode all
```

단위 테스트

```bash
./gradlew testDebugUnitTest
```

모듈에 적용된 의존성 확인

```bash
./gradlew :feature:search:dependencies --configuration debugRuntimeClasspath
```

JDK 17로 컴파일됐는지 (`major version: 61`)

```bash
javap -v <모듈>/build/tmp/kotlin-classes/debug/<클래스 경로>.class | grep major
```
