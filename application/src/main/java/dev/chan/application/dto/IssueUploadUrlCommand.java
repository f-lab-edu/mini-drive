package dev.chan.application.dto;

import dev.chan.common.MimeType;
import dev.chan.domain.file.FileName;
import dev.chan.domain.file.FileSize;

public record IssueUploadUrlCommand(
        String driveId,
        String parentId,
        FileSize size,
        FileName fileName,
        MimeType mimeType
) {
    
}
