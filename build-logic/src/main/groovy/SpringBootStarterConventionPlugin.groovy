import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class SpringBootStarterConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('org.springframework.boot')
        project.plugins.apply('convention.java-common')
        project.plugins.apply('convention.spring-bom')

        project.dependencies.with {
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
            add("implementation", "org.springframework.boot:spring-boot-starter")
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }


    }
}
