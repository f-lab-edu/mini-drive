package dev.chan.infrastructure.aws;

import dev.chan.domain.file.PresignedUrlResponse;
import dev.chan.domain.file.PresignedUrlSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3PresignedUrlGenerator {

    private final S3Presigner presigner;

    /**
     * Presigned URL을 생성하고 해당 정보를 포함한 응답 객체(PresignedUrlResponse)로 반환합니다.
     *
     * @param spec Presigned URL 생성을 위한 사양 정보 (버킷명, 키, 메타데이터 등 포함)
     * @return PresignedUrlResponse 생성된 URL, 만료 시각 등 정보를 담은 불변 응답 VO
     */
    public PresignedUrlResponse createPresignedUrl(PresignedUrlSpecification spec) {
        // putObject 요청 생성
        PutObjectRequest pubObjectRequest = buildPutObjectRequest(spec);

        // 만료 시간 생성
        Duration expiredAfter = Duration.ofMinutes(10);
        Instant expiredAt = Instant.now().plus(expiredAfter);

        // PresignedPutObjectRequest - 최종 presingedUrl 생성 요청
        PresignedPutObjectRequest presignedRequest = generatePresignedPutObjectRequest(pubObjectRequest, presigner, expiredAfter);

        if (!presignedRequest.isBrowserExecutable()) {
            log.warn("생성된 Presigned URL이 브라우저에서 실행되지 않을 수 있습니다. spec={}", spec);
        }

        return PresignedUrlResponse.from(spec, presignedRequest.url().toExternalForm(), expiredAt);
    }

    private PresignedPutObjectRequest generatePresignedPutObjectRequest(PutObjectRequest putObjectRequest, S3Presigner presigner, Duration expiredAfter) {
        PutObjectPresignRequest presignedRequest = buildPutObjectPresignRequest(putObjectRequest, expiredAfter);
        return presigner.presignPutObject(presignedRequest);
    }

    /**
     * PutObjectPresignRequest 객체를 생성하여 반환합니다.
     *
     * @param putObjectRequest S3에 객체 업로드 시 사용할 요청 객체로, 버킷 이름, 메타데이터 등을 포함합니다.
     * @param expiredAfter     Presigned URL의 유효 기간
     * @return PutObjectPresignRequest Presigned URL 생성을 위한 최종 요청 객체
     */
    private PutObjectPresignRequest buildPutObjectPresignRequest(PutObjectRequest putObjectRequest, Duration expiredAfter) {
        return PutObjectPresignRequest.builder().
                signatureDuration(expiredAfter)
                .putObjectRequest(putObjectRequest)
                .build();
    }

    /**
     * S3에 업로드할 객체에 대한 PutObjectRequest를 생성합니다.
     *
     * @param spec Presigned URL 생성을 위한 사양 정보 (버킷명, 파일 키, 메타데이터 포함)
     * @return PutObjectRequest S3에 업로드 요청 시 사용할 객체
     */
    private PutObjectRequest buildPutObjectRequest(PresignedUrlSpecification spec) {
        return PutObjectRequest.builder()
                .bucket(spec.bucketName())
                .key(spec.fileKey())
                .metadata(spec.toS3Metadata())
                .build();
    }
}
