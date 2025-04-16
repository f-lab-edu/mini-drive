package dev.chan.api.web.file.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileMetaDataDTO {
    String relativePath;
    String mimeType;
}
