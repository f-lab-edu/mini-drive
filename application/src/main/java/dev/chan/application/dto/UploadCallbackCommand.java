package dev.chan.application.dto;

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