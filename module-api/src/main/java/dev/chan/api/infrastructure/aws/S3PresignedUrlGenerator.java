package dev.chan.api.infrastructure.aws;

import dev.chan.api.config.AwsConfig;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.PresignedUrlResponse;
import dev.chan.api.domain.file.PresignedUrlSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3PresignedUrlGenerator{

    private final S3Presigner presigner;

    public PresignedUrlResponse createPresignedUrl(PresignedUrlSpecification spec) {
        // putObject 요청 생성
        PutObjectRequest pubObjectRequest = buildPutObjectRequest(spec);

        // 만료 시간 생성
        Duration expiredAfter = Duration.ofMinutes(10);
        Instant expiredAt = Instant.now().plus(expiredAfter);

        // PresignedPutObjectRequest - 최종 presingedUrl 생성 요청
        PresignedPutObjectRequest presignedRequest = generatePresignedPutObjectRequest(pubObjectRequest, presigner,expiredAfter);

        if (presignedRequest.isBrowserExecutable()){
            // TODO : 브라우저 호환성 검사
        }


        return buildResponse(spec, presignedRequest.url(), expiredAt);


    }

     private PresignedPutObjectRequest generatePresignedPutObjectRequest(PutObjectRequest putObjectRequest, S3Presigner presigner, Duration expiredAfter){
         PutObjectPresignRequest presignedRequest = buildPutObjectPresignRequest(putObjectRequest,expiredAfter);
         return presigner.presignPutObject(presignedRequest);
     }

    /**
     * pubObjectPresignedRequest 객체를 생성하여 반환
     *
     * @param putObjectRequest - S3에 객체 업로드시 사용하는 객체. bucket, Metatdata 등의 정보를 담고있다.
     * @param expiredAfter
     * @return
     */
    private PutObjectPresignRequest buildPutObjectPresignRequest(PutObjectRequest putObjectRequest, Duration expiredAfter) {
        return PutObjectPresignRequest.builder().
                signatureDuration(expiredAfter)
                .putObjectRequest(putObjectRequest)
                .build();
    }


    private PutObjectRequest buildPutObjectRequest(PresignedUrlSpecification spec) {
        return PutObjectRequest.builder()
                .bucket(spec.bucketName())
                .key(spec.fileKey())
                .metadata(spec.metaData().toMap())
                .build();
    }

    private PresignedUrlResponse buildResponse(PresignedUrlSpecification spec, URL url, Instant expiredAt){
        return  PresignedUrlResponse.from(spec, url.toExternalForm(), expiredAt);

    }
}
