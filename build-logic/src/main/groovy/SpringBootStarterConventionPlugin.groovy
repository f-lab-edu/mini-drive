import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class SpringBootStarterConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('java-library')
        project.plugins.apply('org.springframework.boot')
        final def lombokVersion = "1.18.30"

        project.dependencies.with {
            add("implementation", platform("org.springframework.boot:spring-boot-dependencies:3.2.5"))
            add("implementation", "org.springframework.boot:spring-boot-starter")
            add("compileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("annotationProcessor", "org.projectlombok:lombok:$lombokVersion")
            add("testCompileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("testAnnotationProcessor", "org.projectlombok:lombok:$lombokVersion")
        }

        project.tasks.withType(JavaCompile).configureEach {
            options.encoding = 'UTF-8'
        }

    }
}
