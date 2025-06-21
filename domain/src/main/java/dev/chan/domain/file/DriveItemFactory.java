package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DriveItemFactory {
    private String driveId;
    private String id;
    private MimeType mimeType;
    private String fileKey;
    private long size;

    public static DriveItem createFrom(String driveId, MimeType mimeType, long size, String fileName) {
        return DriveItem.builder()
                .driveId(driveId)
                .metadata(new FileMetadata(mimeType, fileName, size))
                .build();
    }

    public static DriveItem createFolder(String driveId, MimeType mimeType, String fileName) {
        return DriveItem.builder()
                .driveId(driveId)
                .metadata(new FileMetadata(mimeType, fileName))
                .build();
    }

}
