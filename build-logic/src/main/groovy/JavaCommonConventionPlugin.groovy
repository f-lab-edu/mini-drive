import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion

class JavaCommonConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('java')

        final def lombokVersion = "1.18.30"
        final def jacksonVersion = "2.17.0"

        project.dependencies.with {
            add("compileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("annotationProcessor", "org.projectlombok:lombok:$lombokVersion")
            add("testCompileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("testAnnotationProcessor", "org.projectlombok:lombok:$lombokVersion")

            add("implementation", "com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
            add("implementation", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
            add("implementation", "com.fasterxml.jackson.module:jackson-module-parameter-names:$jacksonVersion")
        }

        project.extensions.getByType(JavaPluginExtension).toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }

        project.tasks.withType(JavaCompile).configureEach {
            options.encoding = 'UTF-8'
        }
    }


}
