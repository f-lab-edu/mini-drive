import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class SpringBootConventionPlugin implements Plugin<Project> {
    void apply(Project project) {
        final def lombokVersion = "1.18.30"

        project.plugins.apply('org.springframework.boot')
        project.plugins.apply('java')

        project.dependencies.with {
            add("implementation", "org.springframework.boot:spring-boot-starter")
            add("implementation", "org.springframework.boot:spring-boot-starter-web")
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
            add("implementation","org.springframework.boot:spring-boot-starter-validation")

            add("compileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("annotationProcessor", "org.projectlombok:lombok:$lombokVersion")
            add("testCompileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("testAnnotationProcessor", "org.projectlombok:lombok:$lombokVersion")

            add("testImplementation", "org.testcontainers:localstack")
            add("testImplementation", "org.testcontainers:junit-jupiter")
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }
    }
}
