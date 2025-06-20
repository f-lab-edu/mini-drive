package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DriveItemFactory {
    private String driveId;
    private String id;
    private MimeType mimeType;
    private String fileKey;
    private long size;

    public static DriveItem createFrom(String driveId, String id, MimeType mimeType, long size, String name) {
        return DriveItem.builder()
                .id(UUID.randomUUID().toString())
                .driveId(driveId)
                .mimeType(mimeType)
                .size(size)
                .name(name)
                .build();
    }

    public static DriveItem createFrom(String driveId, MimeType mimeType, String name) {
        return DriveItem.builder()
                .driveId(driveId)
                .mimeType(mimeType)
                .name(name)
                .build();
    }

}
