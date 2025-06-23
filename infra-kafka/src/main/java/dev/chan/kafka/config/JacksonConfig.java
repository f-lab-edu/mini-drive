package dev.chan.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.common.JacksonSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Jackson ObjectMapper Bean 설정
     *
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return JacksonSupport.basicMapper();
    }
}
