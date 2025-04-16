package dev.chan.api.web.file.response;

import dev.chan.api.domain.file.FileMetaData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FileUploadResponse {
    private String name;
    private List<FileMetaData> metaDataList;
}
