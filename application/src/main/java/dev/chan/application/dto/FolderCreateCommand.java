package dev.chan.application.dto;


import dev.chan.common.MimeType;

public record FolderCreateCommand(
        String driveId,
        String parentId,
        MimeType mimeType,
        String fileName
) {
}
