package dev.chan.domain.file;


public record FileKeySpecification(String driveId, String uploadPrefix, String name) {

    public static FileKeySpecification toKeySpec(String driveId, String name, String uploadPrefix) {
        return new FileKeySpecification(driveId, uploadPrefix, name);
    }
}
