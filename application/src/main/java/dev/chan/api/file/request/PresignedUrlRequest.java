package dev.chan.api.file.request;

import dev.chan.application.command.PresignedUrlCommand;
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
