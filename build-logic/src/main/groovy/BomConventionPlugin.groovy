import org.gradle.api.Plugin
import org.gradle.api.Project

class BomConventionPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.pluginManager.apply("java")

        project.dependencies.with {
            add("implementation", platform("org.springframework.boot:spring-boot-dependencies:3.2.5"))
            add("implementation", platform("software.amazon.awssdk:bom:2.27.3"))
            add("testImplementation", platform("org.testcontainers:testcontainers-bom:1.19.3"))
        }
    }
}
