package dev.chan.infrastructure.aws;

import dev.chan.application.command.PresignedUrlCommand;
import dev.chan.domain.file.FileMetaData;

import java.util.Map;

public record PresignedUrlSpecification(
        String bucketName,
        String driveId,
        String parentId,
        String fileKey,
        FileMetaData metaData) {
    public static PresignedUrlSpecification toUrlSpec(String bucketName, PresignedUrlCommand command, String fileKey, FileMetaData meta) {
        return new PresignedUrlSpecification(bucketName, command.getDriveId(), command.getParentId(), fileKey, meta);
    }

    public Map<String, String> toS3Metadata() {
        return Map.of("driveId", driveId,
                "parentId", parentId,
                "fileKey", fileKey,
                "size", String.valueOf(metaData.getSize()),
                "mimeType", metaData.getMimeType().getMime(),
                "fileName", metaData.getName());
    }
}

