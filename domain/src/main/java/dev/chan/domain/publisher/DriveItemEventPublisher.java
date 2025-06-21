package dev.chan.domain.publisher;

public interface DriveItemEventPublisher {
    void publish(Object event);
}
