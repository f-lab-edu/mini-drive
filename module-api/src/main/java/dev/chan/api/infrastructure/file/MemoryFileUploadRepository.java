package dev.chan.api.infrastructure.file;

import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.FileUploadRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryFileUploadRepository implements FileUploadRepository {

    ConcurrentHashMap<String, DriveItem> repository = new ConcurrentHashMap<>();

    public List<? extends DriveItem> saveAll(List<? extends DriveItem> items) {
        items.forEach(item -> repository.put(UUID.randomUUID().toString(), item));
        return new ArrayList<>(repository.values());
    }

}
