package dev.chan.consumer.service;

import dev.chan.application.file.DriveItemService;
import dev.chan.consumer.dto.UploadCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UploadEventHandler {

    private final DriveItemService driveItemService;

    public void handle(UploadCompletedEvent event) {
        driveItemService.registerUploadedFileIfNotExists(event.toCommand());
    }
}
