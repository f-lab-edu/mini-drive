package dev.chan.infrastructure.file;

import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemRepository;
import dev.chan.domain.file.MimeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fake 레포지토리
 */
@Slf4j
@Repository
public class MemoryDriveItemRepository implements DriveItemRepository {

    private final ConcurrentHashMap<String, DriveItem> repository = new ConcurrentHashMap<>();

    @Override
    public List<DriveItem> saveAll(List<DriveItem> items) {
        items.forEach(item -> repository.put(UUID.randomUUID().toString(), item));
        return new ArrayList<>(repository.values());
    }

    @Override
    public DriveItem save(DriveItem driveItem) {
        if (driveItem == null) {
            throw new IllegalArgumentException("driveItem must not be null");
        }

        DriveItem newItem = new DriveItem(
                driveItem.getDriveId(),
                UUID.randomUUID().toString(),
                driveItem.getName(),
                driveItem.getMimeType(),
                driveItem.getFileKey(),
                driveItem.getSize(),
                driveItem.getParent()
        );

        driveItem.copyOf(newItem);
        repository.put(driveItem.getId(), driveItem);

        log.info("[MemoryDriveItemRepository.save()] - save drive item {}", driveItem);
        return driveItem;
    }

    @Override
    public DriveItem findRootFolder() {
        return new DriveItem("d1234", null, "root", MimeType.FOLDER, "", 1234L, null);
    }

    @Override
    public List<DriveItem> findAllByParentId(String parentId) {
        return List.of();
    }

    @Override
    public Optional<DriveItem> findById(String id) {
        log.info("[findById()] - itemId[{}]", id);
        DriveItem driveItem = repository.get(id);

        log.info("[findById()] - found item {}", driveItem);
        return Optional.ofNullable(driveItem);
    }

    @Override
    public Optional<DriveItem> findParentByParentId(String parentId) {
        return Optional.empty();
    }

    @Override
    public Optional<DriveItem> findByFileKey(String fileKey) {
        log.info("[findByFileKey()] - fileKey[{}]", fileKey);
        log.info("[findByFileKey()] - values()={}", repository.values());
        return repository.values().stream()
                .filter(item -> item.getFileKey().equals(fileKey))
                .findFirst();
    }
}
