package dev.chan.api.domain.file.exception;

public class DriveItemNotFoundException extends RuntimeException {
    // TODO : 추후 에러코드 관련 로직 추가 예정
    public DriveItemNotFoundException(String parentId) {
        super("존재하지 않는 아이템입니다. [" + parentId + "]");
    }
}
