package dev.chan.api.domain.file;

import lombok.Getter;

public record PresignedUrlSpecification (
    String bucketName,
    String fileKey,
    FileMetaData metaData)
{
    public PresignedUrlSpecification(String bucketName, String fileKey, FileMetaData metaData) {
        this.bucketName = bucketName;
        this.fileKey = fileKey;
        this.metaData = metaData;
    }


}

