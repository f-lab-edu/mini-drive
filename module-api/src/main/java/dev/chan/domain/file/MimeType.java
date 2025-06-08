package dev.chan.domain.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.chan.common.exception.UnSupportedMimeTypeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;


/**
 * Drive 에서 제공하는 MIME 타입을 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum MimeType {
    // mini-drive
    FOLDER("application/vnd.mini-drive.folder", ""),

    // 문서
    WORD_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    OPEN_DOCUMENT_TEXT("application/vnd.oasis.opendocument.text", ".odt"),
    RTF("application/rtf", ".rtf"),
    PDF("application/pdf", ".pdf"),
    TEXT("text/plain", ".txt"),
    HTML_ZIP("application/zip", ".zip"), // 웹페이지 (압축됨)
    EPUB("application/epub+zip", ".epub"),
    MARKDOWN("text/markdown", ".md"),

    // 스프레드시트
    EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    OPEN_DOCUMENT_SHEET("application/x-vnd.oasis.opendocument.spreadsheet", ".ods"),
    CSV("text/csv", ".csv"),
    TSV("text/tab-separated-values", ".tsv"),

    // 프레젠테이션
    POWERPOINT_PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx"),
    ODP("application/vnd.oasis.opendocument.presentation", ".odp"),

    // 이미지 (슬라이드/드로잉)
    JPEG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    SVG("image/svg+xml", ".svg");

    private final String mime;
    private final String extension;

    /**
     * mime 타입을 반환합니다.
     *
     * @return mime 타입
     */
    @JsonValue
    public String getMime() {
        return mime;
    }

    /**
     * 문자열 mime 타입을 받아 해당하는 MimeType을 반환합니다.
     *
     * @return mime 타입 문자열
     */
    @JsonCreator
    public static MimeType from(String mime) {
        return Arrays.stream(values())
                .filter(m -> m.mime.equalsIgnoreCase(mime)).findFirst().orElseThrow(() -> new UnSupportedMimeTypeException(mime));
    }

    /**
     * 파일 확장자를 받아 해당하는 MimeType을 반환합니다.
     *
     * @param ext 파일 확장자
     * @return 해당하는 MimeType, 없으면 Optional.empty()
     */
    public static Optional<MimeType> fromExtension(String ext) {
        return Arrays.stream(values())
                .filter(m -> m.extension.equalsIgnoreCase(ext))
                .findFirst();
    }
}
