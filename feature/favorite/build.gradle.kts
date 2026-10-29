import org.gradle.kotlin.dsl.project

plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.feature)
}

android {
    namespace = "com.example.favorite"
}