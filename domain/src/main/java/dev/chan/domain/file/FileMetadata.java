package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FileMetadata {
    private final MimeType mimeType;
    private final FileName fileName;
    private final FileSize fileSize;

    @Builder
    public FileMetadata(MimeType mimeType, String fileName, Long fileSize) {
        this.mimeType = mimeType;
        this.fileName = new FileName(fileName);
        this.fileSize = new FileSize(fileSize);
    }

    public FileMetadata(MimeType mimeType, String fileName) {
        this.mimeType = mimeType;
        this.fileName = new FileName(fileName);
        this.fileSize = new FileSize();
    }

    public static FileMetadata ofFolder(MimeType mimeType, String fileName) {
        return new FileMetadata(mimeType, fileName);
    }

    public static FileMetadata ofRoot() {
        return FileMetadata.builder()
                .mimeType(MimeType.FOLDER)
                .fileName(FileName.ROOT_NAME)
                .fileSize(null)
                .build();
    }

    public long size() {
        return this.fileSize.getBytes();
    }

    public String sizeToString() {
        return this.fileSize.toString();
    }

    public String contentType() {
        return this.mimeType.getMime();
    }

    public String fileName() {
        return this.fileName.getFullName();
    }


}
