package dev.chan.api.application.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.application.file.key.FileKeyGenerator;
import dev.chan.api.config.FileStorageProperties;
import dev.chan.api.domain.file.FileKeySpecification;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.PresignedUrlSpecification;
import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final FileKeyGenerator fileKeyGenerator;
    private final PresignedUrlGenerator urlGenerator;
    private final FileStorageProperties properties;

    public List<String> generateUploadUrls(PresignedUrlCommand presignedUrlCommand) {

        List<FileMetaDataDto> fileMetaDataDtoList = presignedUrlCommand.getFileMetaDataDtoList();

        List<MultipartFile> multipartFiles = presignedUrlCommand.getMultipartFiles();

        List<String> presignedUrls = new ArrayList<>();
        for (int i = 0; i < fileMetaDataDtoList.size(); i++) {
            FileMetaDataDto fileMetaDataDto = fileMetaDataDtoList.get(i);
            MultipartFile multipartFile = multipartFiles.get(i);

            FileKeySpecification fileKeySpecification = new FileKeySpecification(
                    presignedUrlCommand.getDriveId(),
                    properties.getUploadPrefix(),
                    multipartFile.getOriginalFilename());

            String fileKey = fileKeyGenerator.generateFileKey(fileKeySpecification);

            FileMetaData fileMetaData = FileMetaData.of(fileMetaDataDto, multipartFile, presignedUrlCommand.getParentId(), fileKey);

            PresignedUrlSpecification presignedUrlSpecification = new PresignedUrlSpecification(
                    properties.getBucketName(),
                    fileKey,
                    fileMetaData
            );

            presignedUrls.add(urlGenerator.createPresignedUrl(presignedUrlSpecification));
        }

        return presignedUrls;
    }
}
