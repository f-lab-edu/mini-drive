package dev.chan.api.application.file.factory;

import dev.chan.api.application.file.command.UploadCallbackCommand;
import dev.chan.api.domain.file.DriveItem;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DriveItemFactory {
    private String id;
    private String driveId;
    private String mimeType;
    private String fileKey;
    private long size;

    public static DriveItem createFrom(UploadCallbackCommand command) {
        return DriveItem.builder()
                .id(UUID.randomUUID().toString())
                .driveId(command.getDriveId())
                .mimeType(command.getMimeType())
                .fileKey(command.getFileKey())
                .size(command.getSize())
                .name(command.getFileName())
                .build();
    }


}
