package dev.chan.domain.file;

import lombok.Getter;

@Getter
public final class DriveRootItem {

    /**
     * root 인스턴스 생성 방지
     */
    private DriveRootItem() {
    }

    public static DriveItem create(String driveId) {
        return DriveItem.builder()
                .id("root")
                .name("root")
                .driveId(driveId)
                .parent(null)
                .build();
    }
}
