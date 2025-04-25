package dev.chan.api.infrastructure.aws;

import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsProperties;
import dev.chan.api.domain.file.FileMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3PresignedUrlGenerator {

    private final S3KeyGenerator s3KeyGenerator;
    private final AwsProperties awsProperties;

    /**
     * presignedUrl 생성 함수
     * @param driveId - 드라이브 ID
     * @param metaData - 파일 메타데이터
     * @return String presignedUrl
     */
    public String createPresignedUrl(String driveId, FileMetaData metaData) {
        try (S3Presigner s3Presigner = S3Presigner.create()) {

            String bucketName = awsProperties.getBucketName();
            String fileKey = s3KeyGenerator.generateFileKey(awsProperties.getUploadPrefix(), driveId, metaData.getOriginalFileName());

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .metadata(metaData.toMap())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder().
                    signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            String myURL = presignedRequest.url().toString();

            log.info("Presigned URL to upload a file to: [{}]", myURL);
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }
}
