package dev.chan.api.application.file.factory;

import dev.chan.api.application.file.command.FolderCreateCommand;
import dev.chan.api.application.file.command.UploadCallbackCommand;
import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.MimeType;
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
                .id(UUID.randomUUID().toString())
                .driveId(command.driveId())
                .mimeType(command.mimeType())
                .name(command.fileName())
                .build();
    }

}
