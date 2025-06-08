package dev.chan.domain.file;

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
