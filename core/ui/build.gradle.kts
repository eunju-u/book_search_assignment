plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.compose)
}

android {
    namespace = "com.example.ui"
}

dependencies {
    // coil
    implementation(libs.coil.compose)

    // 모듈
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
}