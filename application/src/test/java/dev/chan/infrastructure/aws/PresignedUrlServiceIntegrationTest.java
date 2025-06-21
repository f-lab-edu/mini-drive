package dev.chan.infrastructure.aws;

import dev.chan.application.command.PresignedUrlSpecification;
import dev.chan.application.file.PresignedUrlResponse;
import dev.chan.application.file.S3PresignedUrlGenerator;
import dev.chan.common.MimeType;
import dev.chan.domain.file.FileMetadata;
import dev.chan.infra.config.LocalstackS3Config;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Testcontainers
@Import(LocalstackS3Config.class)
@ActiveProfiles("test")

public class PresignedUrlServiceIntegrationTest {

    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3PresignedUrlGenerator presignedUrlGenerator;

    @PostConstruct
    public void initS3Bucket() {
        s3Client.createBucket(CreateBucketRequest.builder().bucket("test-bucket").build());
    }

    @Test
    @DisplayName("presignedURL을 생성하고, 파일을 업로드한다.")
    void shouldGeneratePresignedUrl_AndUploadFile() throws IOException {
        //given
        PresignedUrlSpecification spec = testSpec();
        String content = "hello localstack!";

        //when
        PresignedUrlResponse response = presignedUrlGenerator.createPresignedUrl(spec);

        HttpURLConnection connection = (HttpURLConnection) URI.create(response.url()).toURL().openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.getOutputStream().write(content.getBytes());

        int responseCode = connection.getResponseCode();

        //then
        assertThat(responseCode).isEqualTo(200);
    }

    @Test
    @DisplayName("로컬스택에서 S3 버킷이 정상 생성된다.")
    void shouldCreateS3Bucket() {
        ListBucketsResponse buckets = s3Client.listBuckets();
        log.info("bucket = {} ", buckets.buckets());
        assertThat(buckets.buckets()).isNotEmpty();
    }


    private static PresignedUrlSpecification testSpec() {
        String bucketName = "test-bucket";
        String driveId = "d1234";
        String parentId = "p1234";
        String key = "test.txt";
        FileMetadata meta = FileMetadata.builder()
                .fileSize(10L)
                .fileName("f1234")
                .mimeType(MimeType.from("text/plain")).build();

        return new PresignedUrlSpecification(bucketName, driveId, parentId, key, meta);
    }
}