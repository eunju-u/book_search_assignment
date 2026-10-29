plugins {
    alias(libs.plugins.booksearch.android.application)
    alias(libs.plugins.booksearch.android.compose)
    alias(libs.plugins.booksearch.android.hilt)
}

android {
    namespace = "com.example.booksearch"

    defaultConfig {
        applicationId = "com.example.booksearch"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // 모듈
    implementation(project(":core:designsystem"))
    implementation(project(":feature:search"))
    implementation(project(":feature:favorite"))
}
