package dev.chan.application.dto;

import dev.chan.domain.file.FileMetadata;

import java.util.Map;

public record PresignedUrlSpecification(
        String bucketName,
        String driveId,
        String parentId,
        String fileKey,
        FileMetadata metaData) {
    public static PresignedUrlSpecification toUrlSpec(String bucketName, PresignedUrlCommand command, String fileKey, FileMetadata meta) {
        return new PresignedUrlSpecification(bucketName, command.getDriveId(), command.getParentId(), fileKey, meta);
    }

    public Map<String, String> toS3Metadata() {
        return Map.of("driveId", driveId,
                "parentId", parentId,
                "fileKey", fileKey,
                "size", metaData.sizeToString(),
                "mimeType", metaData.contentType(),
                "fileName", metaData.fileName());
    }

    public String fileName() {
        return this.metaData.fileName();
    }

    public long size() {
        return this.metaData.size();
    }
}

