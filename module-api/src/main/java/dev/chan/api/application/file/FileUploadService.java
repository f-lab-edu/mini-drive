package dev.chan.api.application.file;

import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.web.file.response.FileUploadResponse;

public interface FileUploadService {
    FileUploadResponse upload(UploadCommand uploadCommand);
}
