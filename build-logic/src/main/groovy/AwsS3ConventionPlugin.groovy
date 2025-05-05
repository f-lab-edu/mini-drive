import org.gradle.api.Plugin
import org.gradle.api.Project

class AwsS3ConventionPlugin implements Plugin<Project>{

    void apply(Project project) {
        project.pluginManager.apply("java")

        project.repositories {
            mavenCentral()
        }

        def awsSdkVersion = '2.27.3'

        def deps = project.dependencies
        deps.add('implementation', deps.platform("software.amazon.awssdk:bom:${awsSdkVersion}"))
        deps.add('implementation', "software.amazon.awssdk:s3")
        deps.add('implementation', "software.amazon.awssdk:secretsmanager")

    }
}
