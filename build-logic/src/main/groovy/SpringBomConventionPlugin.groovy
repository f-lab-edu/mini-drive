import org.gradle.api.Plugin
import org.gradle.api.Project

class SpringBomConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getPlugins().apply("io.spring.dependency-management");

        project.dependencies.with {
            add("implementation", platform("org.springframework.boot:spring-boot-dependencies:3.2.5"))
        }
    }
}
