package dev.chan.api.file.request;

import dev.chan.domain.file.FileMetaData;
import dev.chan.domain.file.MimeType;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaDataDto {
    String name;
    Long size;
    String mimeType;

    public FileMetaData toMetadata() {
        return FileMetaData.builder()
                .name(this.name)
                .size(this.size)
                .mimeType(MimeType.from(this.mimeType))
                .build();
    }
}
