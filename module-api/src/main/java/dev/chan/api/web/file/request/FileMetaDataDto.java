package dev.chan.api.web.file.request;

import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.MimeType;
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

    public FileMetaData toMetadata(){
        return FileMetaData.builder()
                .name(this.name)
                .size(this.size)
                .mimeType(MimeType.fromMime(this.mimeType))
                .build();
    }
}
