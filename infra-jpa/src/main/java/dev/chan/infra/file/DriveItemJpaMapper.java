package dev.chan.infra.file;

import dev.chan.domain.file.DriveItem;

import java.util.ArrayList;


public class DriveItemJpaMapper {

    /**
     * 도메인을 JPA Entity로 변환
     *
     * @param item
     * @return
     */
    public static DriveItemJpaEntity toJpaDriveItemEntity(DriveItem item) {
        return DriveItemJpaEntity.builder()
                .id(item.getId())
                .driveId(item.getDriveId())
                .fileName(item.getFileName())
                .size(item.getSize())
                .mimeType(item.getMimeType())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .createdBy(item.getCreatedBy())
                .build();
    }

    /**
     * JPA Entity를 DriveItem 도메인으로 변환
     *
     * @param entity
     * @return
     */
    public static DriveItem toDomain(DriveItemJpaEntity entity) {
        return DriveItem.of(
                entity.getId(),
                entity.getDriveId(),
                entity.getFileName(),
                entity.getSize(),
                entity.getMimeType().getMime(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getParent(),
                new ArrayList<>()
        );

    }
}
