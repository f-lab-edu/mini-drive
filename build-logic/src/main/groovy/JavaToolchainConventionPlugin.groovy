import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

class JavaToolchainConventionPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.plugins.withId("java") {
            project.extensions.getByType(JavaPluginExtension).toolchain {
                languageVersion = JavaLanguageVersion.of(21)
            }
        }
    }
}