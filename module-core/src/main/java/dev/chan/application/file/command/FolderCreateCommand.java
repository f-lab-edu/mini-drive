package dev.chan.application.file.command;


import dev.chan.common.MimeType;

public record FolderCreateCommand(
        String driveId,
        String parentId,
        MimeType mimeType,
        String name
) {
}
