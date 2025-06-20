package dev.chan.kafka.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.kafka.event.UploadCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadEventParser {

    private final ObjectMapper objectMapper;

    public UploadCompletedEvent parse(String json) {
        try {
            return objectMapper.readValue(json, UploadCompletedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
