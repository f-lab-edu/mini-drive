import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip
import com.github.gradle.node.npm.task.NpmInstallTask

class LambdaConventionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.plugins.apply('base')
        project.plugins.apply('com.github.node-gradle.node')

        project.extensions.configure(com.github.gradle.node.NodeExtension) { node ->
            node.version = '18.8.0'
            node.download = true
        }

        project.afterEvaluate {
            def lambdaModule = project.findProject(':module-lambda')
            def scriptsDir = lambdaModule.layout.projectDirectory.dir("scripts")
            def srcDir = lambdaModule.layout.projectDirectory.dir("src")
            def zipDir = lambdaModule.layout.buildDirectory.dir("lambda-zip")

            project.tasks.named('npmInstall', NpmInstallTask) {
                group = "Lambda"
                description = "Lambda 의존성 설치"
                workingDir.set(srcDir.file("package.json"))
                args.set(["--omit=dev"])
            }

            project.tasks.register('zipLambda', Zip) {
                dependsOn("npmInstall")
                group = "Lambda"
                description = "Lambda 코드를 zip으로 패키징"

                from(srcDir) {
                    include "index.mjs"
                    include "node_modules/**"
                }

                archiveFileName.set("index.zip")
                destinationDirectory.set(zipDir)

            }

            project.tasks.register('testDeployLambda', Exec){
                group ="Lambda"
                description = "Lambda zip 배포"
                dependsOn("zipLambda")
                workingDir=scriptsDir.asFile
                commandLine "sh", "lambda-deploy.sh"
            }
        }
    }
}
