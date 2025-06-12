package dev.chan.infrastructure.aws;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.chan.common.MimeType;

import java.time.Instant;

public record PresignedUrlResponse(
        String driveId,
        String parentId,
        String fileKey,
        String name,
        Long size,
        MimeType mimeType,
        String url,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        Instant expiredAt
) {
    public static PresignedUrlResponse from(PresignedUrlSpecification spec, String url, Instant expiredAt) {
        return new PresignedUrlResponse(
                spec.driveId(),
                spec.parentId(),
                spec.fileKey(),
                spec.metaData().getName(),
                spec.metaData().getSize(),
                spec.metaData().getMimeType(),
                url,
                expiredAt
        );
    }
}
