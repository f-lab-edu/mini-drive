package dev.chan.api.infrastructure.aws;

import dev.chan.api.application.file.PresignedUrlGenerator;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.PresignedUrlSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3PresignedUrlGenerator implements PresignedUrlGenerator {

    @Override
    public String createPresignedUrl(PresignedUrlSpecification urlSpecification) {
        try (S3Presigner s3Presigner = S3Presigner.create()) {
            PutObjectRequest objectRequest = getPutObjectRequest(
                    urlSpecification.fileKey(),
                    urlSpecification.metaData(),
                    urlSpecification.bucketName());

            PutObjectPresignRequest presignRequest = getPutObjectPresignRequest(objectRequest);
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            String myURL = presignedRequest.url().toString();
            log.info("Presigned URL to upload a file to: [{}]", myURL);
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());
            return presignedRequest.url().toExternalForm();
        }
    }

    private PutObjectRequest getPutObjectRequest(String fileKey, FileMetaData metaData, String bucketName) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .metadata(metaData.toMap())
                .build();
    }

    private PutObjectPresignRequest getPutObjectPresignRequest(PutObjectRequest objectRequest) {
        return PutObjectPresignRequest.builder().
                signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();
    }


}
