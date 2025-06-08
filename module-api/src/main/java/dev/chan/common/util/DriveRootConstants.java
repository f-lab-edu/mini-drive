package dev.chan.common.util;

import lombok.Getter;

@Getter
public final class DriveRootConstants {

    private DriveRootConstants() {
        // 인스턴스화 방지
    }

    public static final String ROOT_FOLDER_ID = "root";
    public static final String ROOT_FOLDER_NAME = "root";


    public static String resolveParentId(String providedParentId) {
        if (providedParentId == null || providedParentId.isEmpty()) {
            return ROOT_FOLDER_ID;
        }
        return providedParentId;
    }
}
