package dev.chan.api.domain.file;

import java.util.List;

public interface FileUploadRepository {
    List<? extends DriveItem> saveAll(List<? extends DriveItem> items);
}
