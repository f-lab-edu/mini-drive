package dev.chan.api.application.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsProperties;
import dev.chan.api.config.FileStorageProperties;
import dev.chan.api.domain.file.FileKeySpecification;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.PresignedUrlResponse;
import dev.chan.api.domain.file.PresignedUrlSpecification;
import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final S3KeyGenerator keyGenerator;
    private final S3PresignedUrlGenerator urlGenerator;
    private final AwsProperties properties;


    /**
     * presignedUrl을 생성해 List로 반환합니다.
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
