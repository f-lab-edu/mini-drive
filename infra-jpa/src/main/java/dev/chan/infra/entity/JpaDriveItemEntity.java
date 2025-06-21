package dev.chan.infra.entity;

import dev.chan.common.MimeType;
import dev.chan.domain.file.DriveItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class JpaDriveItemEntity {
    private UUID id;
    private String fileName;
    private MimeType mimeType;
    private String driveId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private Long size;

    private DriveItem parent;
}
