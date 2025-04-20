package dev.chan.api.application.file;

import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.FileUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileStorage fileStorage;
    private final FileUploadRepository fileUploadRepository;
    private final ThumbnailEventPublisher thumbnailEventPublisher;

    public List<FileMetaData> upload(UploadCommand uploadCommand) {
        List<FileMetaData> metaDataList = fileStorage.storeAll(uploadCommand.getFiles(), uploadCommand.getDriveId());
        fileUploadRepository.saveAll(List.of());

        try{
            thumbnailEventPublisher.publish(metaDataList);
        } catch (RuntimeException e) {
            log.warn("썸네일 생성 이벤트 발행 오류. {}",e.getMessage() , e);
        }

        return metaDataList;
    }
}
