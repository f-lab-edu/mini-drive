package dev.chan.application.file;

import dev.chan.application.command.FolderCreateCommand;
import dev.chan.application.command.UploadCallbackCommand;
import dev.chan.application.exception.DriveItemNotFoundException;
import dev.chan.domain.UploadedFileRegisteredEvent;
import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemFactory;
import dev.chan.domain.file.DriveItemRepository;
import dev.chan.domain.file.DriveRootItem;
import dev.chan.domain.publisher.DriveItemEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class DriveItemService {

    private final DriveItemRepository driveItemRepository;
    private final DriveItemEventPublisher domainEventPublisher;

    /**
     * @param command
     * @return
     */
    public DriveItem registerUploadedFile(UploadCallbackCommand command) {
        log.info("[registerUploadedFile()] {}", command);

        // parent 생성
        DriveItem parent = driveItemRepository.findById(command.parentId())
                .orElseGet(() -> DriveRootItem.create(command.driveId()));

        DriveItem createdItem = DriveItemFactory.createFrom(
                command.driveId(),
                command.id(),
                command.mimeType(),
                command.size(),
                command.fileName());

        createdItem.moveTo(parent);
        DriveItem savedItem = driveItemRepository.save(createdItem);

        // 썸네일 fileId 추론 및 생성

        // 파일 아이템 생성 완료 이벤트 등록
        domainEventPublisher.publish(new UploadedFileRegisteredEvent(createdItem.getDriveId(),
                createdItem.getParentId(),
                createdItem.getId()));

        // 파일 아이템 상태 등록
        return savedItem;
    }

    /**
     * 폴더를 생성합니다.
     *
     * @param command
     * @return 생성된 폴더 아이템
     */
    public DriveItem createFolder(FolderCreateCommand command) {
        log.info("[createFolder()] {}", command);

        DriveItem item = DriveItemFactory.createFrom(
                command.driveId(),
                command.mimeType(),
                command.name());

        driveItemRepository.save(item);
        return driveItemRepository.findById(item.getId())
                .orElseThrow(() -> new DriveItemNotFoundException(item.getId()));
    }

    /**
     * 업로드된 파일이 존재하지 않을 경우, 파일을 등록합니다.
     *
     * @param uploadCallbackCommand

    public void registerUploadedFileIfNotExists(UploadCallbackCommand uploadCallbackCommand) {
    log.info("[registerUploadedFileIfNotExists()] {}", uploadCallbackCommand);

    driveItemRepository.findByFileKey(uploadCallbackCommand.fileKey())
    .orElseGet(() -> registerUploadedFile(uploadCallbackCommand));
    }*/
}


