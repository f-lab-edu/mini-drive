package dev.chan.api.domain.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.application.file.command.UploadCallbackCommand;
import lombok.Getter;

public record PresignedUrlSpecification(
        String bucketName,
        String driveId,
        String parentId,
        String fileKey,
        FileMetaData metaData) {
    public static PresignedUrlSpecification toUrlSpec(String bucketName, PresignedUrlCommand command, String fileKey, FileMetaData meta) {
        return new PresignedUrlSpecification(bucketName, command.getDriveId(), command.getParentId(), fileKey, meta);
    }
}

