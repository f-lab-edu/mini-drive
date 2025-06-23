package dev.chan.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
