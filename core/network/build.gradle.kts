import java.util.Properties

plugins {
    alias(libs.plugins.booksearch.android.library)
    alias(libs.plugins.booksearch.android.hilt)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

android {
    namespace = "com.example.network"

    defaultConfig {
        buildConfigField("String", "KAKAO_API_URL", "\"${localProperties.getProperty("KAKAO_API_URL", "")}\"")
        buildConfigField("String", "KAKAO_API_KEY", "\"${localProperties.getProperty("KAKAO_API_KEY", "")}\"")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)
}
