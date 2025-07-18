import com.github.gradle.node.npm.task.NpmInstallTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip

class LambdaConventionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.plugins.apply('base')
        project.plugins.apply('com.github.node-gradle.node')

        project.extensions.configure(com.github.gradle.node.NodeExtension) { node ->
            node.version = '18.8.0'
            node.download = true
        }

        def lambdaModule = project.findProject(':infra-lambda')

        def scriptsDir = lambdaModule.layout.projectDirectory.dir("scripts")
        def srcDir = lambdaModule.layout.projectDirectory.dir("src")
        def zipDir = lambdaModule.layout.buildDirectory.dir("lambda-zip")
        def deployDir = zipDir.get().asFile.absolutePath

        def endPointUrl = "http://localhost:4566"
        def functionName = "uploadCallbackFunction"
        def region = "ap-northeast-2"
        def zipPath = deployDir + "/index.zip"

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

            println "[zipLambda] Lambda zip 패키징 시작"

            from(srcDir) {
                include "index.js"
                include "node_modules/**"
            }

            doFirst {
                println "[zipLambda] 📦 Lambda 압축 대상 경로: ${zipDir.get().asFile}"
            }

            archiveFileName.set("index.zip")
            destinationDirectory.set(zipDir.get())
        }

        project.tasks.register('invokeLambda', Exec) {
            group = "Lambda"
            description = "Lambda 함수 호출"

            workingDir = scriptsDir.asFile

            println "endPointUrl     " + endPointUrl

            environment "ENDPOINT_URL", endPointUrl
            environment "ZIP_PATH", zipPath
            environment "REGION", region

            commandLine "sh", "invoke-lambda.sh"
        }

        project.tasks.register('testDeployLambda', Exec) {
            group = "Lambda"
            description = "Lambda zip 배포"

            println "Lambda zip 배포 시작"
            println "[zipPath]         " + zipPath
            println "[endPointUrl]     " + endPointUrl
            println "[functionName]    " + functionName
            println "[region]          " + region


            workingDir = scriptsDir.asFile

            environment "ENDPOINT_URL", endPointUrl
            environment "ZIP_PATH", zipPath
            environment "FUNCTION_NAME", functionName
            environment "REGION", region

            commandLine "sh", "lambda-deploy.sh"

            standardOutput = System.out
            errorOutput = System.err
        }
    }
}
