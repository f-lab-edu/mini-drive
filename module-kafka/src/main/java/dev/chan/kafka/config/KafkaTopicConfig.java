package dev.chan.kafka.config;

import dev.chan.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

//@Configuration
//@EnableKafka
public class KafkaTopicConfig {

    @Bean
    public NewTopic uploadCompletedCallbackTopic() {
        return TopicBuilder.name(KafkaTopics.UPLOAD_COMPLETED_CALLBACK)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic imageThumbnailGenerationRequestTopic() {
        return TopicBuilder.name(KafkaTopics.IMG_THUMBNAIL_GENERATION_REQUEST)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic documentThumbnailGenerationRequestTopic() {
        return TopicBuilder.name(KafkaTopics.DOC_THUMBNAIL_GENERATION_REQUEST)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic previewGenerationRequestTopic() { // 신규 토픽 빈 추가
        return TopicBuilder.name(KafkaTopics.PREVIEW_GENERATION_REQUEST)
                .partitions(3)
                .replicas(1)
                .build();
    }


}
