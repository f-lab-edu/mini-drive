package dev.chan.api.application.file;

import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsProperties;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.FileUploadRepository;
import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileStorage fileStorage;
    private final FileUploadRepository fileUploadRepository;

    public List<FileMetaData> upload(UploadCommand uploadCommand) {
        List<FileMetaData> metaDataList = fileStorage.storeAll(uploadCommand.getFiles(), uploadCommand.getDriveId());
        fileUploadRepository.saveAll(List.of());
        return metaDataList;
    }
}

