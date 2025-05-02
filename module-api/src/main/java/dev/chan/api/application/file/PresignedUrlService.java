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


    public List<PresignedUrlResponse> generateUploadUrls(PresignedUrlCommand presignedUrlCommand) {
        log.info("PresignedUrlCommand = {}", presignedUrlCommand);

        return presignedUrlCommand.getFileMetaDataDtoList().parallelStream().map(metaDto -> {
            FileKeySpecification keySpecification = new FileKeySpecification(
                    presignedUrlCommand.getDriveId(),
                    properties.getUploadPrefix(),
                    metaDto.getName());

            log.info("keySpecification={}", keySpecification);

            String key = keyGenerator.generateFileKey(keySpecification);

            log.info("key={}", key);

            PresignedUrlSpecification UrlSpecification = new PresignedUrlSpecification(
                    properties.getBucketName(),
                    presignedUrlCommand.getDriveId(),
                    presignedUrlCommand.getParentId(),
                    key,
                    metaDto.toMetadata());

            log.info("UrlSpecification={}", UrlSpecification);

            return urlGenerator.createPresignedUrl(UrlSpecification);

        }).toList();
    }
/*
    public List<String> generateUploadUrls(PresignedUrlCommand presignedUrlCommand) {
        List<FileMetaDataDto> fileMetaDataDtoList = presignedUrlCommand.getFileMetaDataDtoList();

        List<String> presignedUrls = new ArrayList<>();
        for (int i = 0; i < fileMetaDataDtoList.size(); i++) {
            FileMetaDataDto fileMetaDataDto = fileMetaDataDtoList.get(i);

            FileKeySpecification fileKeySpecification = new FileKeySpecification(
                    presignedUrlCommand.getDriveId(),
                    properties.getUploadPrefix(),
                    presignedUrlCommand.getName()
                    );

            String fileKey = keyGenerator.generateFileKey(fileKeySpecification);

            FileMetaData fileMetaData = FileMetaData.of(fileMetaDataDto, presignedUrlCommand.getParentId());

            PresignedUrlSpecification presignedUrlSpecification = new PresignedUrlSpecification(
                    properties.getBucketName(),
                    fileKey,
                    fileMetaData
            );

            presignedUrls.add(urlGenerator.createPresignedUrl(presignedUrlSpecification));
        }

        return presignedUrls;
    }*/
}
