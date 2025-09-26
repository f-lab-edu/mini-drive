package dev.chan.api.file.dto;

import dev.chan.application.dto.PresignedUrlCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PresignedUrlRequest {

    private String driveId;
    private String parentId;
    private List<FileMetaDataDto> fileMetaDataDtoList;

    public PresignedUrlCommand toPresignedUrlCommand() {
        return new PresignedUrlCommand(
                driveId,
                parentId,
                fileMetaDataDtoList
        );
    }
}
