package dev.chan.application.file.command;

import dev.chan.common.util.DriveRootConstants;
import dev.chan.domain.file.MimeType;

public record UploadCallbackCommand(
        MimeType mimeType,
        String driveId,
        String parentId,
        String fileKey,
        String fileName,
        long size

) {

    public FolderCreateCommand toFolderCreateCommand() {
        return new FolderCreateCommand(
                this.driveId,
                DriveRootConstants.ROOT_FOLDER_ID,
                MimeType.FOLDER,
                this.fileName
        );
    }

}