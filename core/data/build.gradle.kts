plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.hilt)
}

android {
    namespace = "com.example.data"
}

dependencies {
    implementation(libs.retrofit)

    // 모듈
    implementation(project(":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))
}