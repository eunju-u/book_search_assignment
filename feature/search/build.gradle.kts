plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.feature)
}

android {
    namespace = "com.example.search"
}

dependencies {
    implementation(libs.androidx.paging.compose)
}