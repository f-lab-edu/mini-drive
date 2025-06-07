import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class SpringBootWebConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('org.springframework.boot')
        project.plugins.apply('java')
        project.plugins.apply('convention.spring-boot-starter')

        project.dependencies.with {
            add("implementation", "org.springframework.boot:spring-boot-starter-web")
            add("implementation","org.springframework.boot:spring-boot-starter-validation")
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }
    }
}
