import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

class LocalstackConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply('base')

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

            def bucketName="testbucket"
            def endPointUrl="http://localhost:4566"
            def region = "ap-northeast-2"

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

                def queueName = "uploadCallbackQueue"

                workingDir = localstackDir.dir("scripts")

                environment "QUEUE_NAME",queueName

                commandLine "sh", "create-upload-callback-queue.sh"

                standardOutput = System.out
                errorOutput = System.err

            }

            project.tasks.register('configS3Notification', Exec) {
                group = "Localstack"
                description = "S3 -> SQS 이벤트 알림 연결"

                workingDir.set(localstackDir.dir("init"))

                commandLine "sh", "02-create-sqs"

                /*
                dependsOn("createSqsUploadCallbackQueue", "setupS3bucket")
                commandLine "awslocal", "s3api", "put-bucket-notification-configuration",
                        "--bucket", "my-bucket",
                        "--notification-configuration", """
                            {
                                "QueueConfigurations":[
                                    {
                                        "QueueArn": "arn:aws:sqs:ap-northeast-2:000000000000:my-queue",
                                        "Events": ["s3:ObjectCreated:*"]
                                    }
                                ]
                            }
                            """*/
            }

            project.tasks.register("uploadTestFile", Exec) {
                group = "localstack"
                description = "objectCreated 이벤트 유도용 테스트 파일 업로드"

                dependsOn("configS3Notification")
                commandLine "awsLocal", "s3", "cp", "hello.txt", "s3://mini-drive/uploads/hello.txt"
            }


        }
    }
}
