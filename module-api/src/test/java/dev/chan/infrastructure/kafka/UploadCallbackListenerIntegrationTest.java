package dev.chan.infrastructure.kafka;

import dev.chan.domain.file.DriveItemRepository;
import dev.chan.kafka.KafkaTopics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.fail;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class UploadCallbackListenerIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void overrideKafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    DriveItemRepository driveItemRepository;

    @Test
    @DisplayName("파일 업로드 후 콜백 리스너가 메시지를 수신하고, DriveItemService의 registerUploadedFile 메서드를 호출한다")
    void shouldPublishUploadCompletedEvent_whenUploadCompleted() throws Exception {
        // given
        String message = """
                {
                  "bucketName": "my-bucket",
                  "fileName": "photo.png",
                  "driveId": "drive-1",
                  "parentId": "folder-1",
                  "fileKey": "key-1",
                  "mimeType": "image/png",
                  "size": 2048
                }
                """;
        //when
        kafkaTemplate.send(KafkaTopics.UPLOAD_COMPLETED_CALLBACK, message);

        // then
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            driveItemRepository.findByFileKey("key-1").ifPresentOrElse(
                    item -> {
                        assertThat(item).isNotNull();
                        assertThat(item.getMimeType().getMime()).isEqualTo("image/png");
                        assertThat(item.getSize()).isEqualTo(2048);
                        assertThat(item.getFileKey()).isEqualTo("key-1");
                    },
                    () -> {
                        fail("Expected DriveItem with fileKey 'key-1' not found");
                    }
            );

        });
    }
}