package dev.chan.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaAdminConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic thumbnailImageCreatedTopic() {
        return TopicBuilder.name("thumbnail.image.created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic thumbnailDocCreatedTopic() {
        return TopicBuilder.name("thumbnail.doc.created")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
