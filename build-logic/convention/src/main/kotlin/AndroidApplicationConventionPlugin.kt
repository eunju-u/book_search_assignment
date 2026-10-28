import com.android.build.api.dsl.ApplicationExtension
import com.example.booksearch.buildlogic.configureAndroid
import com.example.booksearch.buildlogic.findVersionInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// 앱 모듈용
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
                defaultConfig.targetSdk = findVersionInt("targetSdk")
            }
        }
    }
}
