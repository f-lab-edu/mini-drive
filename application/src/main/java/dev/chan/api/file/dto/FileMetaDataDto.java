package dev.chan.api.file.dto;

import dev.chan.domain.file.FileMetadata;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaDataDto {
    String fileName;
    Long size;
    String mimeType;

    public FileMetadata toMetadata() {
        return FileMetadata.of(fileName, size, mimeType);
    }
}
