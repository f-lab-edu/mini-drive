package dev.chan.api.infrastructure.file;

import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.FileUploadRepository;
import org.springframework.stereotype.Repository;


@Repository
public class MemoryFileUploadRepository implements FileUploadRepository {

    @Override
    public FileMetaData storeAll() {
        return null;
    }

}
