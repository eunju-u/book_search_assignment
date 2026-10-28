package com.example.booksearch.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

// 플러그인 코드에서 gradle/libs.versions.toml을 읽기 위한 단축 속성
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

// toml [versions]의 값을 숫자로 읽음
internal fun Project.findVersionInt(name: String): Int =
    libs.findVersion(name).get().requiredVersion.toInt()
