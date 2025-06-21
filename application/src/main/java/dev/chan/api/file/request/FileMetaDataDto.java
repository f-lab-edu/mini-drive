package dev.chan.api.file.request;

import dev.chan.common.MimeType;
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
        return FileMetadata.builder()
                .name(this.name)
                .size(this.size)
                .mimeType(MimeType.from(this.mimeType))
                .build();
    }
}
