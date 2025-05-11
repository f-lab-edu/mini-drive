package dev.chan.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "spring.kafka")
public class KafkaProperties {

    private String bootstrapServers;

}
