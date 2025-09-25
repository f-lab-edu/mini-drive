package dev.chan.application;

import dev.chan.application.dto.IssueUploadUrlCommand;
import dev.chan.common.MimeType;
import dev.chan.domain.file.FileName;
import dev.chan.domain.file.FileSize;

public class CommandMother {

    public static IssueUploadUrlCommand IssueUploadUrlCommand() {
        return new IssueUploadUrlCommand(
                "driveId",
                "parentId",
                new FileSize(1024),
                new FileName("fileName.png"),
                MimeType.from("image/png"));
    }

}
