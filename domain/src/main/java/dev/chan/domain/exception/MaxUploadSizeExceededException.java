package dev.chan.domain.exception;

public class MaxUploadSizeExceededException extends DomainException {
    public MaxUploadSizeExceededException(ErrorCode errorCode) {
        super(errorCode);
    }
}
