package dev.chan.api.domain.file;

import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class FileMetaData {
    private String mimeType;
    private String name;
    private long size;

    public Map<String, String> toMap() {
        return Map.of(
                "size", String.valueOf(size),
                "mimeType", mimeType,
                "name", name);
    }


}
