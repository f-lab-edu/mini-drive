package dev.chan.application.file;

import dev.chan.domain.file.FileMetaData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public interface FileStorage {

    FileMetaData store(MultipartFile file, String fileKey);

    default List<FileMetaData> storeAll(List<MultipartFile> files, String fileKey) {
        return files.stream()
                .map(file -> store(file, fileKey))
                .collect(Collectors.toList());
    }

    ;
}
