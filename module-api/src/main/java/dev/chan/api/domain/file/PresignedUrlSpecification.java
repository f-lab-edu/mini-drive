package dev.chan.api.domain.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.application.file.command.UploadCallbackCommand;
import lombok.Getter;
import lombok.Value;

import java.util.Map;

public record PresignedUrlSpecification(
        String bucketName,
        String driveId,
        String parentId,
        String fileKey,
        FileMetaData meta) {
    public static PresignedUrlSpecification toUrlSpec(String bucketName, PresignedUrlCommand command, String fileKey, FileMetaData meta) {
        return new PresignedUrlSpecification(bucketName, command.getDriveId(), command.getParentId(), fileKey, meta);
    }

    public Map<String,String> toS3Metadata(){
        return Map.of("driveId",driveId,
                "parentId",parentId,
                "fileKey",fileKey,
                "size", String.valueOf(meta.getSize()),
                "mimeType",meta.getMimeType(),
                "name",meta.getName());
    }
}

