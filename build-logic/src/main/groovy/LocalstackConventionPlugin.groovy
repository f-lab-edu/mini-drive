import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

class LocalstackConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('base')

        def bucketName = "testbucket"
        def endPointUrl = "http://localhost:4566"
        def region = "ap-northeast-2"
        def queueName = "upload-callback-queue"

        def testModule = project.findProject(":module-integration-test")
        def localstackDir = testModule.layout.projectDirectory.dir("localstack")

        // 로컬스택 컨테이너 업
        project.tasks.register('localstackUp', Exec) {
            group = "Localstack"
            description = "Localstack 컨테이너를 백그라운드로 실행"

            if (testModule) {
                workingDir = localstackDir
                commandLine "sh", "run.sh"

                standardOutput = System.out
                errorOutput = System.err

                ignoreExitValue = false
            } else {
                doFirst {
                    project.logger.warn(":module-localstack 프로젝트를 찾을 수 없습니다.")
                }
                enabled = false // 모듈이 없으면 task 비활성화
            }
        }

        // 로컬스택 컨테이너 다운
        project.tasks.register('localstackDown', Exec) {
            group = "Localstack"
            description = "Localstack 컨테이너 종료"

            if (testModule) {
                workingDir = localstackDir
                commandLine "sh", "stop.sh"
                standardOutput = System.out
                errorOutput = System.err
                ignoreExitValue = true

            } else {
                doFirst {
                    project.logger.warn(":module-localstack 프로젝트를 찾을 수 없습니다.")
                }
                enabled = false
            }
        }

        project.tasks.register("createBucket", Exec) {
            group = "localstack"
            description = "s3 테스트 버킷 생성"

            workingDir = localstackDir.dir("scripts")

            environment "BUCKET_NAME", bucketName
            environment "ENDPOINT_URL", endPointUrl
            environment "REGION", region

            commandLine "sh", "create-bucket.sh"

            standardOutput = System.out
            errorOutput = System.err
        }

        project.tasks.register('createSqsUploadCallbackQueue', Exec) {
            group = "Localstack"
            description = "sqs upload callback queue 생성"

            workingDir = localstackDir.dir("scripts")

            environment "BUCKET_NAME", bucketName
            environment "QUEUE_NAME", queueName

            commandLine "sh", "create-upload-callback-queue.sh"

            standardOutput = System.out
            errorOutput = System.err
        }

        def fileName = "trigger.txt"
        def uploadPrefix = "uploads"

        project.tasks.register("uploadDummyFile", Exec) {
            group = "localstack"
            description = "objectCreated 이벤트 유도용 더미 파일 업로드"

            workingDir = localstackDir.dir("scripts")

            commandLine "sh", "-c", """
                      echo "trigger" > ${fileName} && \
                      awslocal s3 cp ${fileName} s3://${bucketName}/${uploadPrefix}/${fileName} \
                      --endpoint-url=${endPointUrl}
                    """.stripIndent()

            standardOutput = System.out
            errorOutput = System.err
        }

        def outputFile = project.layout.buildDirectory.file("queue-arn.txt")
        project.tasks.register("getQueueArn", Exec) {
            group = "localstack"
            description = "SQS ARN 조회"

            environment "REGION", region
            environment "ENDPOINT_URL", endPointUrl

            commandLine "sh", "-c", """
                awslocal sqs get-queue-attributes \
                    --queue-url ${endPointUrl}/000000000000/${queueName} \
                    --attribute-name QueueArn \
                    --region ${region} \
                    --endpoint-url ${endPointUrl} \
                    | jq -r '.Attributes.QueueArn' > ${outputFile.get().asFile.absolutePath}
            """.stripIndent()

            outputs.file(outputFile) // 다른 태스크와 연결하기 위한 선언

            doLast {
                println " [Queue Arn] : " + outputFile.get().asFile.text.trim()
            }

            standardOutput = System.out
            errorOutput = System.err
        }

        def functionName = "uploadCallbackFunction"
        project.tasks.register("mapSqsToLambda", Exec) {
            group = "localstack"
            description = "Lambda 와 SQS 를 매핑하는 Event Source Mapping 생성"

            dependsOn('getQueueArn')

            doFirst {
                def queueArn = outputFile.get().asFile.text.trim()
                def command = """
                    echo ${functionName}
                    if ! awslocal lambda list-event-source-mappings \\
                      --function-name ${functionName} \\
                      --region $region \\
                      --endpoint-url=${endPointUrl} | grep ${queueArn}; then
            
                      echo '🔗 연결이 없어 새로 생성합니다...\'
                      awslocal lambda create-event-source-mapping \\
                        --function-name ${functionName} \\
                        --batch-size 1 \\
                        --event-source-arn ${queueArn} \\
                        --region ${region} \\
                        --endpoint-url=${endPointUrl}
            
                    else
                      echo '✅ 이미 연결된 SQS입니다. 생성 생략.\\'
                    fi
                """.stripIndent()

                commandLine "sh", "-c", command
            }

            standardOutput = System.out
            errorOutput = System.err
        }

        project.tasks.register('waitLambdaActive', Exec) {
            group = "localstack"
            description = "Lambda 가 Active 될 때 까지 대기"

            commandLine "sh", "-c", """
                echo '⌛ Lambda 활성화 대기중...'
                awslocal lambda wait function-active-v2 --function-name ${functionName} 
            """.stripIndent()

            standardOutput = System.out
            errorOutput = System.err
        }

        project.tasks.register("checkLambdaExecutionLogs", Exec) {
            group = "localstack"
            description = "Lambda 실행 로그 확인"

            commandLine "sh", "-c", """
                echo "📘 전체 로그 출력 (raw)"
                awslocal logs filter-log-events --log-group-name /aws/lambda/${functionName}
            """.stripIndent()

            standardOutput = System.out
            errorOutput = System.err

        }

    }
}
