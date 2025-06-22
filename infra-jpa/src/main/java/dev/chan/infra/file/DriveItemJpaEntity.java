package dev.chan.infra.file;

import dev.chan.common.MimeType;
import dev.chan.domain.file.DriveItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class DriveItemJpaEntity {
    private UUID id;
    private String fileName;
    private MimeType mimeType;
    private Long size;
    private String driveId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    private DriveItem parent;
}
