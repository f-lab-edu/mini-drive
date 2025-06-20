package dev.chan.domain.file;

import java.util.List;
import java.util.Optional;


public interface DriveItemRepository {
    List<DriveItem> saveAll(List<DriveItem> items);

    DriveItem save(DriveItem driveItem);

    DriveItem findRootFolder();

    List<DriveItem> findAllByParentId(String parentId);

    Optional<DriveItem> findById(String parentId);

    Optional<DriveItem> findParentByParentId(String parentId);

    Optional<DriveItem> findByFileKey(String fileKey);

}
