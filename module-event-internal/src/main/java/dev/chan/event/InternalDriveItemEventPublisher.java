package dev.chan.event;

import dev.chan.domain.publisher.DriveItemEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternalDriveItemEventPublisher implements DriveItemEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Object event) {

    }
}
