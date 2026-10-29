plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.detail"
}

dependencies {
    // coil
    implementation(libs.coil.compose)
}