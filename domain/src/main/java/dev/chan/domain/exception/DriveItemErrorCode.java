package dev.chan.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DriveItemErrorCode implements ErrorCode {

    MAX_FILE_SIZE_EXCEED("error.drive.MAX_FILE_SIZE_EXCEED", 403),
    ;

    private final String message;
    private final int status;


    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
