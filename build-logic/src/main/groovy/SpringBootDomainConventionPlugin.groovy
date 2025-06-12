import org.gradle.api.Plugin
import org.gradle.api.Project

class SpringBootDomainConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('convention.java-common');
        project.plugins.apply('convention.spring-bom')

        project.dependencies.with {
            add('api', 'org.springframework.boot:spring-boot-starter-data-jpa')
            add('runtimeOnly', 'com.h2database:h2')
        }
        
    }
}
