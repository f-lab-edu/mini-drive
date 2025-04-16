package dev.chan.api.application.file;

import dev.chan.api.domain.file.FileMetaData;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public interface FileStorage {

    FileMetaData store(MultipartFile file);

    default List<FileMetaData> storeAll(List<MultipartFile> files){
        return files.stream()
                .map(this::store)
                .collect(Collectors.toList());
    };
}
