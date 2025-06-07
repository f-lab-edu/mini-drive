import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestcontainersConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.dependencies.with {
            add("testImplementation", platform("org.testcontainers:testcontainers-bom:1.19.3"))
            add('testImplementation', "org.testcontainers:junit-jupiter")
            add('testImplementation', "org.testcontainers:localstack")
            add('testImplementation', "org.testcontainers:kafka")
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
            systemProperties(
                    'logging.level.org.testcontainers': 'WARN',
                    'logging.level.org.testcontainers.shaded': 'WARN',
                    'logging.level.com.github.dockerjava': 'WARN',
                    'logging.level.okhttp3': 'WARN',
                    'logging.level.org.apache.kafka': 'WARN',
                    'logging.level.kafka': 'WARN',
                    'logging.level.org.apache.zookeeper': 'WARN',
                    'logging.level.com.amazon.a.l.a.s.SystemSettingLoader': 'WARN',
                    'logging.level.com.amazonaws': 'WARN'
            )
        }
    }
}
