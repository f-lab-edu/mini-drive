import org.gradle.api.Plugin
import org.gradle.api.Project

class AwsS3ConventionPlugin implements Plugin<Project>{

    void apply(Project project) {
        project.pluginManager.apply("java")
        project.dependencies.with {
            add("implementation", platform("software.amazon.awssdk:bom:2.27.3"))
            add("implementation", "software.amazon.awssdk:s3")
            add("implementation", "software.amazon.awssdk:secretsmanager")
        }
    }
}
