package dev.chan.infrastructure.aws;

import dev.chan.application.command.PresignedUrlSpecification;
import dev.chan.application.file.PresignedUrlResponse;
import dev.chan.application.file.S3PresignedUrlGenerator;
import dev.chan.common.MimeType;
import dev.chan.domain.file.FileMetadata;
import dev.chan.infra.config.AwsProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    @DisplayName("driveId와 metaData로 URL을 생성한다.")
    void shouldGenerateUrl_whenDriveIdWithMetadata() {
        // given
        String fileKey = "test/test.pdf";

        FileMetadata metaData = FileMetadata.builder()
                .fileName("test.pdf")
                .fileSize(10L)
                .mimeType(MimeType.PDF.getMime())
                .build();

        PresignedUrlSpecification presignedUrlSpecification = new PresignedUrlSpecification(awsProperties.getBucketName(),
                "d1234",
                "root",
                fileKey,
                metaData);
        // when
        PresignedUrlResponse generatedUrl = generator.createPresignedUrl(
                presignedUrlSpecification
        );

        // then
        assertThat(generatedUrl).isNotNull();

        Instant now = Instant.now();
        assertThat(generatedUrl.expiredAt())
                .isAfter(now.plusSeconds(50))
                .isBefore(now.plus(Duration.ofMinutes(10)));
    }
}