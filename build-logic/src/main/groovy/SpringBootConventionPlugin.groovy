import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class SpringBootConventionPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.plugins.apply('org.springframework.boot')
        project.plugins.apply('java')
        project.plugins.apply('io.spring.dependency-management')

        def testContainersVersion = "1.19.3"

        project.dependencies.with {
            add("implementation", "org.springframework.boot:spring-boot-starter")
            add("implementation", "org.springframework.boot:spring-boot-starter-web")
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
            add("implementation","org.springframework.boot:spring-boot-starter-validation")
            add("compileOnly", "org.projectlombok:lombok")
            add("annotationProcessor", "org.projectlombok:lombok")
            add("testCompileOnly", "org.projectlombok:lombok")
            add("testAnnotationProcessor", "org.projectlombok:lombok")
            add("testImplementation", "org.testcontainers:localstack:$testContainersVersion")
            add("testImplementation", "org.testcontainers:junit-jupiter:$testContainersVersion")
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }

    }
}
