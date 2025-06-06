package dev.chan.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalFileStorage {
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