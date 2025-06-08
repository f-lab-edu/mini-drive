package dev.chan.application.file.command;


import dev.chan.api.file.request.FileMetaDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class PresignedUrlCommand {
    private String driveId;
    private String parentId;
    private List<FileMetaDataDto> fileMetaDataDtoList;

    /**
     * 테스트용 메테데이터 생성 메서드
     *
     * @param count
     * @return
     */
    public static PresignedUrlCommand of(int count) {
        List<FileMetaDataDto> metaList = IntStream.range(0, count)
                .mapToObj(i -> new FileMetaDataDto("file" + i + ".txt", 1024L, "text/plain"))
                .toList();
        return new PresignedUrlCommand("drive-123", "parent-123", metaList);
    }
}
