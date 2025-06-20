package dev.chan.infra.mapper;

import dev.chan.domain.file.DriveItem;
import dev.chan.infra.entity.JpaDriveItemEntity;

import java.util.ArrayList;

public class DriveItemJpaMapper {
    public static JpaDriveItemEntity toJpaDriveItemEntity(DriveItem item) {
        return JpaDriveItemEntity.builder()
                .id(item.getId())
                .driveId(item.getDriveId())
                .name(item.getName())
                .size(item.getSize())
                .mimeType(item.getMimeType())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .createdBy(item.getCreatedBy())
                .build();
    }

    public static DriveItem toDomain(JpaDriveItemEntity entity) {
        return new DriveItem(
                entity.getId(),
                entity.getDriveId(),
                entity.getName(),
                entity.getSize(),
                entity.getMimeType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                null,
                new ArrayList<>()
        );

    }
}
