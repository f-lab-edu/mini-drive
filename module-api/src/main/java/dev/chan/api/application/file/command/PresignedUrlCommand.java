package dev.chan.api.application.file.command;


import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class PresignedUrlCommand {
    private String driveId;
    private String parentId;
    private List<FileMetaDataDto> fileMetaDataDtoList;
    private List<MultipartFile> multipartFiles;

}
