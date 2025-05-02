package dev.chan.api.domain.file;

import lombok.Getter;

public record PresignedUrlSpecification (
    String bucketName,
    String driveId,
    String parentId,
    String fileKey,
    FileMetaData metaData)
{
    public PresignedUrlSpecification(String bucketName, String driveId, String parentId, String fileKey, FileMetaData metaData) {
        this.bucketName = bucketName;
        this.driveId = driveId;
        this.parentId = parentId;
        this.fileKey = fileKey;
        this.metaData = metaData;
    }


}

