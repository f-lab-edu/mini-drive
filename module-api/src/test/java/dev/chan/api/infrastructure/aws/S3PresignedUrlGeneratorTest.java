package dev.chan.api.infrastructure.aws;

import dev.chan.api.config.AwsProperties;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.PresignedUrlSpecification;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class S3PresignedUrlGeneratorTest {

    @Autowired
    private S3PresignedUrlGenerator generator;

    @Autowired
    AwsProperties awsProperties;

    @PostConstruct
    public void init() {
        log.info("awsProperties {}", awsProperties);
        log.info("AWS S3 Bucket: {}", awsProperties.getS3().getBucket());
        log.info("AWS Region: {}", awsProperties.getRegion());
    }
    @Test
    @DisplayName("")
    void shouldGenerateUrl_whenDriveId(){
        // given
        awsProperties.getBucketName();

        // when
        log.info("bucketName {}", awsProperties.getBucketName());
        // then
        assertThat(awsProperties).isNotNull();
        assertThat(awsProperties.getBucketName()).isNotNull();

    }

    @Test
    @DisplayName("driveId와 metaData로 URL을 생성한다.")
    void shouldGenerateUrl_whenDriveIdWithMetadata(){
        // given
        String fileKey = "test/test.pdf";

        FileMetaData metaData = FileMetaData.builder()
                .originalFileName("test")
                .relativePath("")
                .size(10)
                .parentId("root")
                .mimeType("application/pdf")
                .originalFileName("test.pdf")
                .build();

        // when
        String generatedUrl = generator.createPresignedUrl(
                new PresignedUrlSpecification(
                        awsProperties.getBucketName(),
                        fileKey,
                        metaData)
        );
        
        // then
        assertThat(generatedUrl).isNotNull()
                .contains(fileKey)
                .contains(LocalDate.now().toString());
    }
}