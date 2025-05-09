package dev.chan.api.web.file.request;

import dev.chan.api.domain.file.MimeType;
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
}
