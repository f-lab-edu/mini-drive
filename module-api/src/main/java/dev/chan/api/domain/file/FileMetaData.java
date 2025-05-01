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
    private String relativePath;
    private String mimeType;

    private long size;
    private String parentId;
    private String originalFileName;
    private String fileKey;

    public static FileMetaData of(FileMetaDataDto dto, MultipartFile file, String parentId, String fileKey) {
        return FileMetaData.builder()
                .relativePath(dto.getRelativePath())
                .mimeType(dto.getMimeType())
                .size(file.getSize())
                .fileKey(fileKey)
                .parentId(parentId)
                .originalFileName(file.getOriginalFilename())
                .build();
    }

    public Map<String, String> toMap() {
        return Map.of(
                "relativePath", relativePath,
                "size", String.valueOf(size),
                "parentId", parentId,
                "mimeType", mimeType,
                "fileKey", fileKey,
                "originalFileName", originalFileName);
    }


}
