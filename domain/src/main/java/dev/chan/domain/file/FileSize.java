package dev.chan.domain.file;

import dev.chan.domain.exception.DriveItemErrorCode;
import dev.chan.domain.exception.MaxUploadSizeExceededException;
import lombok.Getter;

@Getter
public class FileSize {

    // TODO 추후 외부 유효성 검사로 분리 예정입니다. - 런타임 정책 수정을 위해 ConfigurationProperties 로 관리 예정
    private static final long MAX_UPLOAD_SIZE = 2L * 1024 * 1024 * 1024;
    private final long bytes;

    /**
     * 폴더 생성용 생성자입니다.
     */
    public FileSize() {
        this.bytes = 0L;
    }

    /**
     * 파일 사이즈 초기화 생성자
     * 파일 사이즈는 2GB 파일까지 허용합니다.
     *
     * @param bytes - 파일 바이너리데이터 사이즈
     */
    public FileSize(long bytes) {
        if (bytes < 0) throw new IllegalArgumentException("파일 크기는 음수가 될 수 없습니다.");
        if (bytes > MAX_UPLOAD_SIZE)
            throw new MaxUploadSizeExceededException(DriveItemErrorCode.MAX_FILE_SIZE_EXCEED);
        this.bytes = bytes;
    }

}
