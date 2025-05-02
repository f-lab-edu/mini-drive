package dev.chan.api.infrastructure.storage;

import dev.chan.api.application.file.FileStorage;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.FileStorageProperties;
import dev.chan.api.domain.file.FileKeySpecification;
import dev.chan.api.domain.file.FileMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class LocalFileStorage{
    /*
    private final S3KeyGenerator fileKeyGenerator;
    // private final FileStorageProperties properties;

    private  ConcurrentHashMap<String, FileMetaData> storage = new ConcurrentHashMap<>();

    @Override
    public FileMetaData store(MultipartFile file, String driveId) {
        String baseDir = properties.getUploadPrefix();
        String fileKey = fileKeyGenerator.generateFileKey(new FileKeySpecification(baseDir, driveId, file.getOriginalFilename()));

        FileMetaData metaData = FileMetaData.builder()
                .size(file.getSize())
                .name(file.getOriginalFilename())
                .build();

        storage.put(metaData.getFileKey(),metaData);

        return metaData;
    }*/

}