import com.example.booksearch.buildlogic.configureKotlinJvm
import com.example.booksearch.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

// 순수 Kotlin 모듈용
class JvmLibraryConventionPlugin : Plugin<Project> {
    // Kotlin JVM 플러그인 적용 후 JDK 17, 테스트 의존성 설정
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            configureKotlinJvm()

            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
            }
        }
    }
}
