package dev.chan.application.command;

import dev.chan.common.MimeType;

public record UploadCallbackCommand(
        MimeType mimeType,
        String driveId,
        String parentId,
        String id,
        String fileName,
        long size

) {

}