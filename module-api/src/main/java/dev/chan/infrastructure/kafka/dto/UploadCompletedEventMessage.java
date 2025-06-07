package dev.chan.infrastructure.kafka.dto;

import dev.chan.application.file.command.UploadCallbackCommand;
import dev.chan.domain.file.MimeType;

public record UploadCompletedEventMessage(
        String bucketName,
        String driveId,
        String parentId,
        String fileKey,
        String fileName,
        MimeType mimeType,
        long size

) {
    public UploadCallbackCommand toUploadCallbackCommand() {
        return new UploadCallbackCommand(
                mimeType, driveId, parentId, fileKey, fileName, size
        );
    }
}
