plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.hilt)
}

android {
    namespace = "com.example.database"
}

dependencies {
    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}