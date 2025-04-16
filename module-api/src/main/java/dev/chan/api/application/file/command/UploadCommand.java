package dev.chan.api.application.file.command;

import dev.chan.api.web.file.request.FileMetaDataDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class UploadCommand {

    String driveId;
    String parentId;
    List<MultipartFile> files;
    List<FileMetaDataDTO> entries;

    public UploadCommand(String driveId, String parentId, List<MultipartFile> files, List<FileMetaDataDTO> entries) {
        this.files = files;
        this.driveId = driveId;
        this.parentId = parentId;
        this.entries = entries;
    }
}
