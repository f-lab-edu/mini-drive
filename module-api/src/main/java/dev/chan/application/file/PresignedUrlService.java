package dev.chan.application.file;

import dev.chan.application.file.command.PresignedUrlCommand;
import dev.chan.application.file.key.S3KeyGenerator;
import dev.chan.config.AwsProperties;
import dev.chan.domain.file.FileKeySpecification;
import dev.chan.domain.file.PresignedUrlResponse;
import dev.chan.domain.file.PresignedUrlSpecification;
import dev.chan.infrastructure.aws.S3PresignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final S3KeyGenerator keyGenerator;
    private final S3PresignedUrlGenerator urlGenerator;
    private final AwsProperties properties;


    /**
     * presignedUrl을 생성해 List로 반환합니다.
     *
     * @param presignedUrlCommand
     * @return List<presingedUrlResponse> - presignedUrl을 포함한 클라이언트에 필요한 응답 데이터 리스트
     */
    public List<PresignedUrlResponse> generateUploadUrls(PresignedUrlCommand presignedUrlCommand) {
        log.info("PresignedUrlCommand = {}", presignedUrlCommand);

        return presignedUrlCommand.getFileMetaDataDtoList().stream().map(metaDto -> {
            FileKeySpecification keySpec = FileKeySpecification.toKeySpec(presignedUrlCommand.getDriveId(), metaDto.getName(), properties.getUploadPrefix());

            log.info("keySpecification={}", keySpec);

            String key = keyGenerator.generateFileKey(keySpec);

            log.info("key={}", key);

            PresignedUrlSpecification urlSpec = PresignedUrlSpecification.toUrlSpec(properties.getBucketName(), presignedUrlCommand, key, metaDto.toMetadata());

            log.info("UrlSpecification={}", urlSpec);

            return urlGenerator.createPresignedUrl(urlSpec);
        }).toList();

    }
}
