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

        project.afterEvaluate {

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

            project.tasks.register("createBucket", Exec){
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
                environment "QUEUE_NAME",queueName

                commandLine "sh", "create-upload-callback-queue.sh"

                standardOutput = System.out
                errorOutput = System.err
            }

            def fileName = "trigger.txt"
            def uploadPrefix = "uploads"

            project.tasks.register("uploadDummyFile", Exec) {
                group = "localstack"
                description = "objectCreated 이벤트 유도용 더미 파일 업로드"

                environment "BUCKET_NAME", bucketName
                environment "ENDPOINT_URL", endPointUrl
                environment "FILE_NAME", fileName
                environment "UPLOAD_PREFIX", uploadPrefix

                workingDir = localstackDir.dir("scripts")

                // commandLine "sh", "upload-dummy-file.sh"
                commandLine "sh", "-c", '''
                          echo "trigger" > $FILE_NAME && \
                          awslocal s3 cp $FILE_NAME s3://$BUCKET_NAME/$UPLOAD_PREFIX/$FILE_NAME \
                          --endpoint-url=$ENDPOINT_URL
                        '''

                standardOutput = System.out
                errorOutput = System.err
            }

            project.tasks.register("checkSqsMessage", Exec) {
                group = "localstack"
                description = "SQS 메시지 수신 확인"

                environment "BUCKET_NAME", bucketName
                environment "ENDPOINT_URL", endPointUrl
                environment "QUEUE_NAME", queueName
                environment "REGION", region

                commandLine "sh", "-c", '''
                        awslocal sqs receive-message \
                        --queue-url=$ENDPOINT_URL/000000000000/$QUEUE_NAME \
                        --region=$REGION \
                        --endpoint-url=$ENDPOINT_URL \
                '''

                standardOutput = System.out
                errorOutput = System.err
            }

        }
    }
}
