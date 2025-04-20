package dev.chan.api.application.file;

import dev.chan.api.domain.file.FileMetaData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThumbnailService {

    void generateThumbnails(List<FileMetaData> fileMetaDataList);
}
