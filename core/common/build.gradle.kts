plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.hilt)
}

android {
    namespace = "com.example.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}