package dev.chan.api.infrastructure.file;

import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.DriveItemRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fake 레포지토리
 */
@Repository
public class MemoryDriveItemRepository implements DriveItemRepository {

    ConcurrentHashMap<String, DriveItem> repository = new ConcurrentHashMap<>();
    @Override
    public List<DriveItem> saveAll(List<DriveItem> items) {
        items.forEach(item -> repository.put(UUID.randomUUID().toString(), item));
        return new ArrayList<>(repository.values());
    }

    @Override
    public DriveItem save(DriveItem driveItem) {
        return driveItem;
    }

    @Override
    public DriveItem findRootFolder() {
        return new DriveItem("d1234",null, "root", "root", "application/vnd.mini-drive.folder", "");

    }

    @Override
    public List<DriveItem> findAllByParentId(String parentId) {
        return List.of();
    }

    @Override
    public Optional<DriveItem> findById(String parentId) {
        return Optional.empty();
    }

    @Override
    public Optional<DriveItem> findParentByParentId(String parentId) {
        return Optional.empty();
    }

}
