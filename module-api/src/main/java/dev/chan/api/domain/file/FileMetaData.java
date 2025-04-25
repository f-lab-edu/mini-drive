package dev.chan.api.domain.file;

import dev.chan.api.web.file.request.FileMetaDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FileMetaData {

    private String name;
    private String relativePath;
    private long size;
    private String parentId;
    private String mimeType;
    private String originalFileName;
    private String fileKey;

    public Map<String,String> toMap(){
        return Map.of("name",name
                ,"relativePath",relativePath
                ,"size",String.valueOf(size),
                "parentId",parentId
                ,"mimeType",mimeType
                ,"originalFileName",originalFileName);
    }


}
