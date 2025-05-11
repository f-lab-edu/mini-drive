package dev.chan.api.application.file;

import dev.chan.api.application.file.command.FolderCreateCommand;
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

    public DriveItem registerUploadedFile(UploadCallbackCommand uploadCallbackCommand) {
        DriveItem parent = driveItemRepository.findById(uploadCallbackCommand.parentId())
                .orElseThrow( ()-> new DriveItemNotFoundException(uploadCallbackCommand.parentId()));
        DriveItem createdItem = DriveItemFactory.createFrom(uploadCallbackCommand);
        createdItem.moveTo(parent);
        return driveItemRepository.save(createdItem);
    }

    public DriveItem createFolder(FolderCreateCommand folderCreateCommand) {
        DriveItem item = DriveItemFactory.createFrom(folderCreateCommand);
        driveItemRepository.save(item);
        return driveItemRepository.findById(item.getId())
                .orElseThrow(()-> new DriveItemNotFoundException(item.getId()));
    }

}

