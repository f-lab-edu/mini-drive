package dev.chan.api.infrastructure.storage;

import dev.chan.api.application.file.FileStorage;
import dev.chan.api.domain.file.FileMetaData;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalFileStorage implements FileStorage {

    private  ConcurrentHashMap<String, FileMetaData> storage = new ConcurrentHashMap<>();

    @Override
    public FileMetaData store(MultipartFile file) {

        FileMetaData metaData = FileMetaData.builder()
                .size(file.getSize())
                .name(file.getOriginalFilename())
                .fileKey(UUID.randomUUID().toString())
                .build();

        storage.put(metaData.getFileKey(),metaData);

        return metaData;
    }

}
