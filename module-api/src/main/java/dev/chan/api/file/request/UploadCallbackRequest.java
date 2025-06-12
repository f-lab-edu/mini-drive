package dev.chan.api.file.request;

import dev.chan.application.file.command.UploadCallbackCommand;
import dev.chan.common.MimeType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCallbackRequest {
    private String mimeType;
    String driveId;
    String parentId;
    String fileKey;
    String fileName;
    long size;

    public UploadCallbackCommand toCommand() {
        return new UploadCallbackCommand(MimeType.from(mimeType), driveId, parentId, fileKey, fileName, size);
    }
}
