package dev.chan.application.file;

import dev.chan.application.file.command.FolderCreateCommand;
import dev.chan.application.file.command.UploadCallbackCommand;
import dev.chan.application.file.exception.DriveItemNotFoundException;
import dev.chan.application.file.factory.DriveItemFactory;
import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemRepository;
import dev.chan.domain.support.DriveRoot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class DriveItemService {
    private final DriveItemRepository driveItemRepository;

    /**
     * 업로드된 파일을 등록합니다.
     *
     * @param uploadCallbackCommand
     * @return 등록된 DriveItem
     */
    public DriveItem registerUploadedFile(UploadCallbackCommand uploadCallbackCommand) {
        log.info("[registerUploadedFile()] {}", uploadCallbackCommand);

        DriveItem parent = driveItemRepository.findById(uploadCallbackCommand.parentId())
                .orElseGet(() -> DriveRoot.create(uploadCallbackCommand.driveId()));

        DriveItem createdItem = DriveItemFactory.createFrom(uploadCallbackCommand);
        log.info("createdItem = {}", createdItem);
        createdItem.moveTo(parent);
        return driveItemRepository.save(createdItem);
    }

    /**
     * 폴더를 생성합니다.
     *
     * @param folderCreateCommand
     * @return 생성된 폴더 아이템
     */
    public DriveItem createFolder(FolderCreateCommand folderCreateCommand) {
        log.info("[createFolder()] {}", folderCreateCommand);

        DriveItem item = DriveItemFactory.createFrom(folderCreateCommand);
        driveItemRepository.save(item);
        return driveItemRepository.findById(item.getId())
                .orElseThrow(() -> new DriveItemNotFoundException(item.getId()));
    }

    /**
     * 업로드된 파일이 존재하지 않을 경우, 파일을 등록합니다.
     *
     * @param uploadCallbackCommand
     */
    public void registerUploadedFileIfNotExists(UploadCallbackCommand uploadCallbackCommand) {
        log.info("[registerUploadedFileIfNotExists()] {}", uploadCallbackCommand);

        driveItemRepository.findByFileKey(uploadCallbackCommand.fileKey())
                .orElseGet(() -> registerUploadedFile(uploadCallbackCommand));
    }
}

