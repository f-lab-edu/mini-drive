package dev.chan.kafka.event;

import dev.chan.application.command.UploadCallbackCommand;
import dev.chan.common.MimeType;

public record UploadCompletedEvent(
        String bucketName,
        String driveId,
        String parentId,
        String fileKey,
        String fileName,
        MimeType mimeType,
        long size
) {

    public UploadCallbackCommand toCommand() {
        return new UploadCallbackCommand(
                this.mimeType,
                this.driveId,
                this.parentId,
                this.fileKey,
                this.fileName,
                this.size
        );
    }

}
