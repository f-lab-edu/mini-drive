package dev.chan.api.file.response;

import dev.chan.domain.file.FileMetaData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FileUploadResponse {
    private String driveId;
    private List<FileMetaData> metaDataList;
}
