plugins {
    alias(libs.plugins.booksearch.jvm.library)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
