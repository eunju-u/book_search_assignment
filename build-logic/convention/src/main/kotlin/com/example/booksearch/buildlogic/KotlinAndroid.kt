package com.example.booksearch.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        compileSdk = findVersionInt("compileSdk")
        defaultConfig.minSdk = findVersionInt("minSdk")
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
    configureKotlinJvmTarget()

    dependencies {
        add("testImplementation", libs.findLibrary("junit").get())
        add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
        add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
    }
}

// 순수 Kotlin 모듈 공통 : JDK 17
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    configureKotlinJvmTarget()
}

// Kotlin도 Java와 같은 17로 컴파일 (다르면 JVM target 불일치 오류)
private fun Project.configureKotlinJvmTarget() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
