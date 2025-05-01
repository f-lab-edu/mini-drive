package dev.chan.api.domain.file;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriveItemRepository {
    List<DriveItem> saveAll(List<DriveItem> items);

    DriveItem save(DriveItem driveItem);

    DriveItem findRootFolder();

    List<DriveItem> findAllByParentId(String parentId);

    Optional<DriveItem> findById(String parentId);

    Optional<DriveItem> findParentByParentId(String parentId);
}
