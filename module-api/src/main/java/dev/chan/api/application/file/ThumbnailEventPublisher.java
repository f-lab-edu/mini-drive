package dev.chan.api.application.file;


import dev.chan.api.domain.file.FileMetaData;

import java.util.List;

public interface ThumbnailEventPublisher {

    void publish(List<FileMetaData> metaData);
}
