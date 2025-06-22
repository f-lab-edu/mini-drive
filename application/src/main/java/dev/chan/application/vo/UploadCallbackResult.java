package dev.chan.application.vo;

import dev.chan.domain.file.DriveItem;
import dev.chan.domain.userstate.UserItemState;

public record UploadCallbackResult(
        DriveItem driveItem,
        UserItemState userItemState
) {

}
