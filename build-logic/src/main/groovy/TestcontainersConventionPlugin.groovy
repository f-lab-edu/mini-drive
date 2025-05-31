import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestcontainersConventionPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.dependencies.with {
            add("testImplementation", platform("org.testcontainers:testcontainers-bom:1.19.3"))
            add('testImplementation', "org.testcontainers:junit-jupiter")
            add('testImplementation', "org.testcontainers:localstack")
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }
    }
}
