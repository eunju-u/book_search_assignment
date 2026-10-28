// Top-level build file where you can add configuration options common to all sub-projects/modules.
// build-logic 플러그인이 안에서 적용하는 플러그인은 모두 여기에 apply false로 등록 (버전은 libs.versions.toml)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
