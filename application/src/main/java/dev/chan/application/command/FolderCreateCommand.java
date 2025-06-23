package dev.chan.application.command;


import dev.chan.common.MimeType;

public record FolderCreateCommand(
        String driveId,
        String parentId,
        MimeType mimeType,
        String fileName
) {
}
