package dev.chan.api.application.file.command;

import dev.chan.api.domain.file.MimeType;

public record FolderCreateCommand(
        String driveId,
        String parentId,
        MimeType mimeType,
        String fileName
) {}
