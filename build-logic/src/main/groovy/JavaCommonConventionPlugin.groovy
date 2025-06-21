import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
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

            add("implementation", "jakarta.validation:jakarta.validation-api:3.0.2")
            add("implementation", "org.hibernate.validator:hibernate-validator:8.0.1.Final")

            add("testImplementation", "org.junit.jupiter:junit-jupiter:5.10.2")
            add("testImplementation", "org.mockito:mockito-core:5.12.0")
            add("testImplementation", "org.assertj:assertj-core:3.25.3")


        }

        project.extensions.getByType(JavaPluginExtension).toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }

        project.tasks.withType(JavaCompile).configureEach {
            options.encoding = 'UTF-8'
        }
    }


}
