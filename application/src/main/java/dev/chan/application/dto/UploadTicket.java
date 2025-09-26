package dev.chan.application.dto;

import dev.chan.domain.file.FileMetadata;

public record UploadTicket(String uploadUrl, String fileKey, FileMetadata fileMetadata) {

    public static UploadTicket of(String uploadUrl, String fileKey, FileMetadata fileMetadata) {
        return new UploadTicket(uploadUrl, fileKey, fileMetadata);
    }


}
