package dev.chan.application.command;

public record UploadCallbackCommand(
        String mimeType,
        String driveId,
        String parentId,
        String fileId,
        String fileName,
        long size,
        String userId

) {

}