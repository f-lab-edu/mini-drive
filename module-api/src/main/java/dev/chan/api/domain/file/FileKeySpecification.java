package dev.chan.api.domain.file;


public record FileKeySpecification(
        String driveId,
        String uploadPrefix,
        String name
) {

    public FileKeySpecification (String driveId, String uploadPrefix, String name) {
        this.driveId = driveId;
        this.uploadPrefix = uploadPrefix;
        this.name = name;
    }


}
