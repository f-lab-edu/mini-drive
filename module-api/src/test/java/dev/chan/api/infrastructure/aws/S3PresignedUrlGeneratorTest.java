package dev.chan.api.infrastructure.aws;

import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsConfig;
import dev.chan.api.config.AwsProperties;
import dev.chan.api.domain.file.FileMetaData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.services.s3.model.S3KeyFilter;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        String driveId = "d1234";

        FileMetaData metaData = FileMetaData.builder()
                .name("test")
                .relativePath("")
                .size(10)
                .parentId("root")
                .mimeType("application/pdf")
                .originalFileName("test.pdf")
                .build();

        // when
        String generatedUrl = generator.createPresignedUrl(driveId, metaData);
        
        // then
        assertThat(generatedUrl).isNotNull();
        assertThat(generatedUrl).contains(driveId);
        assertThat(generatedUrl).contains(LocalDate.now().toString());
    }
}