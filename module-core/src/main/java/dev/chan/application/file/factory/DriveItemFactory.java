package dev.chan.application.file.factory;

import dev.chan.application.file.command.FolderCreateCommand;
import dev.chan.application.file.command.UploadCallbackCommand;
import dev.chan.common.MimeType;
import dev.chan.domain.file.DriveItem;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DriveItemFactory {
    private String id;
    private String driveId;
    private MimeType mimeType;
    private String fileKey;
    private long size;

    public static DriveItem createFrom(UploadCallbackCommand command) {
        return DriveItem.builder()
                .id(UUID.randomUUID().toString())
                .driveId(command.driveId())
                .mimeType(command.mimeType())
                .fileKey(command.fileKey())
                .size(command.size())
                .name(command.fileName())
                .build();
    }

    public static DriveItem createFrom(FolderCreateCommand command) {
        return DriveItem.builder()
                .driveId(command.driveId())
                .mimeType(command.mimeType())
                .name(command.name())
                .build();
    }
    
}
