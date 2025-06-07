package dev.chan.kafka.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "spring.kafka")
@RequiredArgsConstructor
public class KafkaProperties {
    
    private final List<String> bootstrapServers;
}
