package dev.chan.api.domain.file;

import dev.chan.api.domain.file.exception.UnSupportedMimeTypeException;

import java.util.Arrays;
import java.util.Optional;

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

    MimeType(String mime, String extension) {
        this.mime = mime;
        this.extension = extension;
    }

    public String getMime() {
        return mime;
    }

    public String getExtension() {
        return extension;
    }

    public static MimeType fromMime(String mime) {
        return Arrays.stream(values())
                .filter(m -> m.mime.equalsIgnoreCase(mime))
                .findFirst().orElseThrow(()-> new UnSupportedMimeTypeException(mime));
    }

    public static Optional<MimeType> fromExtension(String ext) {
        return Arrays.stream(values())
                .filter(m -> m.extension.equalsIgnoreCase(ext))
                .findFirst();
    }
}
