package dev.chan.api.file.dto;

import dev.chan.domain.file.FileMetadata;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FileUploadResponse {
    private String driveId;
    private List<FileMetadata> metaDataList;
}
