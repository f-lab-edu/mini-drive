package dev.chan.api.application.file.command;

import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.FileMetaData;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UploadCallbackCommand {
    private String relativePath;
    private String mimeType;
    private String driveId;
    private String parentId;
    private String fileKey;
    private String fileName;
    private long size;

    public FileMetaData toMetadata() {
        return FileMetaData.builder()
                .size(this.size)
                .parentId(this.parentId)
                .mimeType(this.mimeType)
                .fileKey(this.fileKey)
                .build();
    }

    public DriveItem toFile() {
        return DriveItem.builder()
                .id(UUID.randomUUID().toString())
                .driveId(this.driveId)
                .mimeType(this.mimeType)
                .build();

    }
}