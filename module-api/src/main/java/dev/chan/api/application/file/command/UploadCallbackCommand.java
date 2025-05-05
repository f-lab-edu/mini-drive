package dev.chan.api.application.file.command;

import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.MimeType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
public record UploadCallbackCommand(
     MimeType mimeType,
     String driveId,
     String parentId,
     String fileKey,
     String fileName,
     long size

) {

    public FileMetaData toMetadata() {
        return FileMetaData.builder()
                .size(this.size)
                .mimeType(this.mimeType)
                .build();
    }
}