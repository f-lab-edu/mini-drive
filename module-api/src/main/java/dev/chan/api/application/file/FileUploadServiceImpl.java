package dev.chan.api.application.file;

import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.FileUploadRepository;
import dev.chan.api.web.file.response.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileStorage fileStorage;
    private final FileUploadRepository fileUploadRepository;
    private final ThumbnailEventPublisher thumbnailEventPublisher;

    @Override
    public FileUploadResponse upload(UploadCommand uploadCommand) {
        List<FileMetaData> metaDataList = fileStorage.storeAll(uploadCommand.getFiles(), uploadCommand.getDriveId());
        fileUploadRepository.saveAll(List.of());

        try{
            thumbnailEventPublisher.publish(metaDataList);
        } catch (RuntimeException e) {
            log.warn("썸네일 생성 이벤트 발행 오류. {}",e.getMessage() , e);
        }

        return FileUploadResponse.builder()
                .metaDataList(metaDataList)
                .build();
    }
}
