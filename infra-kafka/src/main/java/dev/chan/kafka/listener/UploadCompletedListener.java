package dev.chan.kafka.listener;

import dev.chan.kafka.KafkaGroups;
import dev.chan.kafka.KafkaTopics;
import dev.chan.kafka.event.UploadCompletedEvent;
import dev.chan.kafka.parser.UploadEventParser;
import dev.chan.kafka.service.UploadEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadCompletedListener {

    private final UploadEventHandler uploadEventHandler;
    private final UploadEventParser uploadEventParser;

    /**
     * Kafka에서 파일 업로드 완료 이벤트를 수신하여 처리합니다.
     *
     * @param message Kafka 메시지
     */
    @KafkaListener(topics = KafkaTopics.UPLOAD_COMPLETED_CALLBACK, groupId = KafkaGroups.FILE_UPLOAD_GROUP)
    public void handleUploadCompleted(String message) {
        UploadCompletedEvent event = uploadEventParser.parse(message);
        uploadEventHandler.handle(event);
    }

    /**
     * Kafka에서 썸네일 생성 이벤트를 수신하여 처리합니다.
     *
     * @param message Kafka 메시지
     */
    @KafkaListener(topics = "thumbnail.created", groupId = "thumbnail-generator-group")
    public void handleCreateThumbnail(String message) {
        // TODO: 썸네일 생성 로직을 구현합니다.
    }

    /**
     * Kafka에서 미리보기 생성 이벤트를 수신하여 처리합니다.
     *
     * @param message Kafka 메시지
     */
    @KafkaListener(topics = "preview.created", groupId = "preview-generator-group")
    public void handleCreatePreview(String message) {
        // TODO: 미리보기 생성 로직을 구현합니다.
    }
}
