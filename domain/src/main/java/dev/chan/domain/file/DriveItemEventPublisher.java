package dev.chan.domain.file;

public interface DriveItemEventPublisher {
    void publish(Object event);
}
