package dev.chan.api.infrastructure.kafka;

import dev.chan.api.application.file.ThumbnailEventPublisher;
import dev.chan.api.domain.file.FileMetaData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaThumbnailPublisher implements ThumbnailEventPublisher {
    @Override
    public void publish(List<FileMetaData> metaData) {

    }
}
