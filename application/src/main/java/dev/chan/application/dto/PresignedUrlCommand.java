package dev.chan.application.dto;


import dev.chan.api.file.dto.FileMetaDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class PresignedUrlCommand {
    private String driveId;
    private String parentId;
    private List<FileMetaDataDto> fileMetaDataDtoList;

}
