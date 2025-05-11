import org.gradle.api.Plugin
import org.gradle.api.Project

class KafkaConventionPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.dependencies.with{
            add("implementation", "org.springframework.kafka:spring-kafka")
            add("testImplementation", "org.springframework.kafka:spring-kafka-test")
        }
    }
}
