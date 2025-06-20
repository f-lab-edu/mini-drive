package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class FileMetaData {
    private MimeType mimeType;
    private String name;
    private long size;
}
