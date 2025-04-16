package dev.chan.api.domain.file;

import dev.chan.api.web.file.request.FileMetaDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FileMetaData {

    String name;
    String fileKey;
    long size;


}
