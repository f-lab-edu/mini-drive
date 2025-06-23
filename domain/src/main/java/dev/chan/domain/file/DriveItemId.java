package dev.chan.domain.file;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DriveItemId {

    private final UUID value;

    public DriveItemId() {
        this.value = UUID.randomUUID();
    }

    public DriveItemId(UUID id) {
        this.value = id;
    }
}
