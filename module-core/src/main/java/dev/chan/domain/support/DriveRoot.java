package dev.chan.domain.support;

import dev.chan.domain.file.DriveItem;
import lombok.Getter;

@Getter
public final class DriveRoot {

    /**
     * root 인스턴스 생성 방지
     */
    private DriveRoot() {
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
