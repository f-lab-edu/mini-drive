package dev.chan.api.domain.folder;

import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.DriveItem;

import java.util.Optional;

public interface FolderRepository {
    DriveItem findById(DriveItem any);

    DriveItem save(DriveItem parentFolder);

    Optional<DriveItem> findById(String parentId);

    DriveItem findRootFolder();
}
