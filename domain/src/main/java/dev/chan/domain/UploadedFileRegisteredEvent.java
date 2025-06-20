package dev.chan.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UploadedFileRegisteredEvent {
    private String driveId;
    private String parentId;
    private String fileId;
}
