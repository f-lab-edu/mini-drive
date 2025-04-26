package dev.chan.api.infrastructure.storage;

import dev.chan.api.application.file.FileStorage;
import dev.chan.api.application.file.key.FileKeyGenerator;
import dev.chan.api.config.FileStorageProperties;
import dev.chan.api.domain.file.FileKeySpecification;
import dev.chan.api.domain.file.FileMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final FileKeyGenerator fileKeyGenerator;
    private final FileStorageProperties properties;

    @Autowired
    public LocalFileStorage(FileStorageProperties properties,FileKeyGenerator fileKeyGenerator) {
        this.properties = properties;
        this.fileKeyGenerator = fileKeyGenerator;
    }

    private  ConcurrentHashMap<String, FileMetaData> storage = new ConcurrentHashMap<>();

    @Override
    public FileMetaData store(MultipartFile file, String driveId) {
        String baseDir = properties.getUploadPrefix();
        String fileKey = fileKeyGenerator.generateFileKey(new FileKeySpecification(baseDir, driveId, file.getOriginalFilename()));

        FileMetaData metaData = FileMetaData.builder()
                .size(file.getSize())
                .originalFileName(file.getOriginalFilename())
                .build();


        storage.put(metaData.getFileKey(),metaData);

        return metaData;
    }

}