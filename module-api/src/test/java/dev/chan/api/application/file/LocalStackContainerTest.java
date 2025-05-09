package dev.chan.api.application.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Testcontainers
public class LocalStackContainerTest {

    /**
     * static final - 클래스 전체에서 한 번만 컨테이너 시작/종료
     * 단 @TestContainers가 붙어있어야하고,
     * - 내부적으로 Junit 의 afterAll, beforeAll 단계에서 컨테이너를 자동 시작 / 종료
     *
     * @Container
     * LocalStackContainer 가 테스터 컨테이너로서 관리한다는 걸 알리기 위해 @Container 가 붙어있어야함.
     * static이 아닐경우 테스트마다 새 컨테이너가 생성됨
     */
    @Container
    static final LocalStackContainer container = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3")
    ).withServices(LocalStackContainer.Service.S3);

    @Test
    void test() {
        // S3 client 생성
        S3Client s3Client = S3Client.builder()
                .region(Region.of(container.getRegion()))
                .endpointOverride(container.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(container.getAccessKey(),container.getSecretKey())
                ))
                .build();

        // 버킷 생성
        String bucketName = "test-bucket";
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        log.info("bucket을 생성했습니다. bucketName={} ", bucketName);
        String content= "Hello localstack";
        String key = "s3-key";

        // client가 url로 파일을 업로드했다고 가정
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(), RequestBody.fromString(content)
        );
        log.info("파일을 업로드하였습니다. bucketName={}, key={}, content={}", bucketName, key, content);

    }

}
