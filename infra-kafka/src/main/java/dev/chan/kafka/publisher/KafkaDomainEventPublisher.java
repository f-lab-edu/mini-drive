package dev.chan.kafka.publisher;

import dev.chan.domain.publisher.DriveItemEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class KafkaDomainEventPublisher implements DriveItemEventPublisher {

    @Override
    public void publish(Object event) {

    }
}
