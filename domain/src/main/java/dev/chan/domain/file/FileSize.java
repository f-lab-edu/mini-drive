package dev.chan.domain.file;

import dev.chan.domain.exception.DriveItemErrorCode;
import dev.chan.domain.exception.MaxUploadSizeExceededException;
import lombok.Getter;

@Getter
public class FileSize {

    /*== 바이트 변환 상수 ==*/
    private static final double GB = 1024.0 * 1024 * 1024;
    private static final double MB = 1024.0 * 1024;
    private static final double KB = 1024.0;

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

    /**
     * 파일 사이즈를 문자열 형태로 리턴
     * 스토리지 제한 용량 표시 및 클라이언트 제공을 위한 편의섬 메서드입니다.
     *
     * @return String 파일 사이즈 EX) 2.00 GB, 2.00 MB, 2.00 KB
     */
    public String readableSize() {
        if (bytes >= GB) return String.format("%.2f GB", bytes / GB);
        else if (bytes >= MB) return String.format("%.2f MB", bytes / MB);
        else if (bytes >= KB) return String.format("%.2f KB", bytes / KB);
        else return String.format("%d B", bytes);
    }

}
