package dev.chan.api.domain.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DriveItem {

    private String id;
    private String name;
    private String mimeType;
    private String relativePath;
    private String driveId;

    private FolderItem parent;


}
