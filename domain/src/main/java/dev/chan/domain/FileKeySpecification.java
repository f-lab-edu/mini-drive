package dev.chan.domain;


public record FileKeySpecification(String driveId, String uploadPrefix, String fileName) {

    public static FileKeySpecification toKeySpec(String driveId, String fileName, String uploadPrefix) {
        return new FileKeySpecification(driveId, uploadPrefix, fileName);
    }
}
