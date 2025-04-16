import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class SpringBootConventionPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.plugins.apply('org.springframework.boot')
        project.plugins.apply('java')
        project.plugins.apply('io.spring.dependency-management')

        project.repositories {
            mavenCentral()
        }

        project.dependencies {
            implementation 'org.springframework.boot:spring-boot-starter-web'
            implementation 'org.springframework.boot:spring-boot-starter'
            testImplementation 'org.springframework.boot:spring-boot-starter-test'

            compileOnly 'org.projectlombok:lombok'
            annotationProcessor 'org.projectlombok:lombok'

            testCompileOnly 'org.projectlombok:lombok'
            testAnnotationProcessor 'org.projectlombok:lombok'
        }

        project.tasks.withType(Test).configureEach {
            useJUnitPlatform()
        }

    }
}
