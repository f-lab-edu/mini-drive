package dev.chan.api.infrastructure.storage;

import dev.chan.api.application.file.FileStorage;
import dev.chan.api.application.file.key.FileKeyGenerator;
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
public class LocalFileStorage implements FileStorage {

    private final String baseDir;
    private final FileKeyGenerator fileKeyGenerator;

    @Autowired
    public LocalFileStorage(@Value("${file.base-dir}") String baseDir,FileKeyGenerator fileKeyGenerator) {
        this.baseDir = baseDir;
        this.fileKeyGenerator = fileKeyGenerator;
    }

    private  ConcurrentHashMap<String, FileMetaData> storage = new ConcurrentHashMap<>();

    @Override
    public FileMetaData store(MultipartFile file, String driveId) {
        String fileKey = fileKeyGenerator.generateFileKey(baseDir,driveId ,file.getOriginalFilename());

        FileMetaData metaData = FileMetaData.builder()
                .size(file.getSize())
                .name(file.getOriginalFilename())
                .fileKey(fileKey)
                .build();


        storage.put(metaData.getFileKey(),metaData);

        return metaData;
    }

}
