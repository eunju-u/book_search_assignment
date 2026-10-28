import com.example.booksearch.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

// Hilt를 쓰는 Android 모듈용
class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // ksp 플러그인 적용.
            pluginManager.apply("com.google.devtools.ksp")

            pluginManager.withPlugin("com.android.base") {
                pluginManager.apply("com.google.dagger.hilt.android")

                dependencies {
                    add("implementation", libs.findLibrary("hilt.android").get())
                    add("ksp", libs.findLibrary("hilt.compiler").get())
                }
            }
        }
    }
}
