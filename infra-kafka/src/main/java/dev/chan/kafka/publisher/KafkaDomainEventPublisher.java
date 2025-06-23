package dev.chan.kafka.publisher;

import dev.chan.domain.file.DriveItemEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class KafkaDomainEventPublisher implements DriveItemEventPublisher {

    @Override
    public void publish(Object event) {

    }
}
