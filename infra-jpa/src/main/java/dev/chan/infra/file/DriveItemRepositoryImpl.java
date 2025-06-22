package dev.chan.infra.file;

import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DriveItemRepositoryImpl implements DriveItemRepository {
    //private final JpaDriveItemRepository jpa;

    @Override
    public List<DriveItem> saveAll(List<DriveItem> items) {
        List<DriveItemJpaEntity> entities = items.stream()
                .map(DriveItemJpaMapper::toJpaDriveItemEntity)
                .toList();
        // List<JpaDriveItemEntity> saved = jpa.saveAll(entities);
        //return saved.stream().map(DriveItemJpaMapper::toDomain).toList();
        return null;
    }

    @Override
    public DriveItem save(DriveItem driveItem) {

        //jpa.save();
        return null;
    }

    @Override
    public DriveItem findRootFolder() {
        return null;
    }

    @Override
    public List<DriveItem> findAllByParentId(String parentId) {
        return List.of();
    }

    @Override
    public Optional<DriveItem> findById(String parentId) {
        log.info("[findById()] {}", parentId);
        return Optional.empty();
    }

    @Override
    public Optional<DriveItem> findParentByParentId(String parentId) {
        return Optional.empty();
    }

    @Override
    public Optional<DriveItem> findByFileKey(String fileKey) {
        return Optional.empty();
    }
}
