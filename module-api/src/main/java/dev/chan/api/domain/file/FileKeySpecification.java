package dev.chan.api.domain.file;


public record FileKeySpecification(
        String driveId,
        String uploadPrefix,
        String originalFilename
) {

    public FileKeySpecification (String driveId, String uploadPrefix, String originalFilename) {
        this.driveId = driveId;
        this.uploadPrefix = uploadPrefix;
        this.originalFilename = originalFilename;
    }


}
