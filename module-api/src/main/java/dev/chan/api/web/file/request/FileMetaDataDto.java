package dev.chan.api.web.file.request;

import dev.chan.api.domain.file.FileMetaData;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaDataDto {
    String relativePath;
    String mimeType;

    public FileMetaData toFileMetaData() {
        return FileMetaData.builder()
                .relativePath(relativePath)
                .mimeType(mimeType)
                .build();
    }
}
