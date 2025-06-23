package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DriveItemFactory {
    private String driveId;
    private String fileId;
    private String fileKey;
    private MimeType mimeType;
    private long size;

    public static DriveItem createFrom(String driveId, DriveItem parent, String fileId, String mimeType, long size, String fileName, String userId) {
        return DriveItem.builder()
                .id(UUID.fromString(fileId))
                .parent(parent)
                .driveId(driveId)
                .createdBy(userId)
                .metadata(new FileMetadata(mimeType, fileName, size))
                .build();
    }

    public static DriveItem createFolder(String driveId, MimeType mimeType, String fileName) {
        return DriveItem.builder()
                .driveId(driveId)
                .metadata(new FileMetadata(mimeType, fileName))
                .build();
    }

    public static DriveItem createFrom(String mimeType, String driveId, DriveItem parent, String fileId, String fileName, long size, String userId) {
        return DriveItem.builder()
                .id(UUID.fromString(fileId))
                .driveId(driveId)
                .parent(parent)
                .metadata(new FileMetadata(mimeType, fileName, size))
                .createdBy(userId)
                .build();

    }
}
