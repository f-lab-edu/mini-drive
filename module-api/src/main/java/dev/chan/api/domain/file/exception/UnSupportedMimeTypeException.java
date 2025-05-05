package dev.chan.api.domain.file.exception;

public class UnSupportedMimeTypeException extends RuntimeException {
  // TODO : 추후 에러코드 관련 로직 추가 예정
  public UnSupportedMimeTypeException(String mimeType) {
    super("지원하지 않는 타입입니다. [" + mimeType + "]");
  }
}
