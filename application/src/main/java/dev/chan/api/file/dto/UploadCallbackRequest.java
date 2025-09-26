package dev.chan.api.file.dto;

import dev.chan.application.dto.UploadCallbackCommand;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCallbackRequest {
    private String mimeType;
    String driveId;
    String userId;
    String parentId;
    String fileKey;
    String fileName;
    long size;

    public UploadCallbackCommand toCommand() {
        return new UploadCallbackCommand(mimeType, driveId, parentId, fileKey, fileName, size, userId);
    }
}
