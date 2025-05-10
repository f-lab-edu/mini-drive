import org.gradle.api.Plugin
import org.gradle.api.Project

class KafkaConventionPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        final def kafkaClientVersion = "4.0.0"

        project.dependencies.with{
            add("implementation","org.apache.kafka:kafka-clients:$kafkaClientVersion")
            add("implementation", "org.springframework.kafka:spring-kafka")
            add("testImplementation", "org.springframework.kafka:spring-kafka-test")
        }
    }
}
