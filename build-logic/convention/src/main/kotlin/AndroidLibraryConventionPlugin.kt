import com.android.build.api.dsl.LibraryExtension
import com.example.booksearch.buildlogic.configureAndroid
import com.example.booksearch.buildlogic.findVersionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// Android 라이브러리 모듈용
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                testOptions.targetSdk = findVersionInt("targetSdk")
                lint.targetSdk = findVersionInt("targetSdk")
            }
        }
    }
}
