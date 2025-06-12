package dev.chan.application.file.command;

import dev.chan.common.MimeType;

public record UploadCallbackCommand(
        MimeType mimeType,
        String driveId,
        String parentId,
        String fileKey,
        String fileName,
        long size

) {

}