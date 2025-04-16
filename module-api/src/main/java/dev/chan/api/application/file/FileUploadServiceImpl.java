package dev.chan.api.application.file;

import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.FileUploadRepository;
import dev.chan.api.web.file.response.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
    @RequiredArgsConstructor
    public class FileUploadServiceImpl implements FileUploadService {

        private final FileStorage fileStorage;
        private final FileUploadRepository fileUploadRepository;

    @Override
    public FileUploadResponse upload(UploadCommand uploadCommand) {
        List<FileMetaData> fileMetaData = fileStorage.storeAll(uploadCommand.getFiles());

        return FileUploadResponse.builder()
                .metaDataList(fileMetaData)
                .build();
    }
}
