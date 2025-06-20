package dev.chan.infra.entity;

import dev.chan.common.MimeType;
import dev.chan.domain.file.DriveItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class JpaDriveItemEntity {
    private String id;
    private String name;
    private MimeType mimeType;
    private String driveId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private Long size;

    private DriveItem parent;
}
