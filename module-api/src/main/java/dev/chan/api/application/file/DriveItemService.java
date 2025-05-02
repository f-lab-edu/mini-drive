package dev.chan.api.application.file;

import dev.chan.api.application.file.command.UploadCallbackCommand;
import dev.chan.api.application.file.factory.DriveItemFactory;
import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.DriveItemRepository;
import dev.chan.api.domain.file.exception.DriveItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class DriveItemService {
    private final DriveItemRepository driveItemRepository;

    public DriveItem create(UploadCallbackCommand uploadCallbackCommand) {
        DriveItem parent = driveItemRepository.findById(uploadCallbackCommand.getParentId())
                .orElseThrow( ()-> new DriveItemNotFoundException(uploadCallbackCommand.getParentId()));
        DriveItem createdItem = DriveItemFactory.createFrom(uploadCallbackCommand);
        createdItem.moveTo(parent);
        return driveItemRepository.save(createdItem);
    }

    /**
     * S3에 저장 완료된 파일의 메타데이터를 DB에 저장한다.
     * @param uploadCallbackCommand
     * @return

    public DriveItem processUploadedFile(UploadCallbackCommand uploadCallbackCommand) {
        if (uploadCallbackCommand.getMimeType().contains("folder")) {
            return folderService.processUploadedFolder(uploadCallbackCommand);
        }

        DriveItem parent = fileUploadRepository.findParentByParentId(uploadCallbackCommand.getParentId())
                .orElseGet(folderRepository::findRootFolder);

        DriveItem file = uploadCallbackCommand.toFile();
        //file.moveTo(parent);
        fileUploadRepository.save(file);
        return file;
    }

    public DriveItem create(UploadCallbackCommand uploadCallbackCommand) {
        return null;
    }    */

        /*
    private FolderItem getOrCreateFolder(DriveItem  parentFolder, String folderName) {
        return parentFolder.findChildrenByName(folderName)
                .filter(FolderItem.class::isInstance)
                .map(FolderItem.class::cast)
                .orElseGet(() -> folderService.create(FolderCommandFactory.toFolderCreateCommand(parentFolder, folderName)));
    }*/
}

