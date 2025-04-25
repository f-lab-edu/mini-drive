package dev.chan.api.application.file.key;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class S3KeyGenerator implements FileKeyGenerator{

    @Override
    public String generateFileKey(String baseDir, String driveId, String originalFilename) {
        return String.format("%s/%s/%s/%s_%s", baseDir, driveId, LocalDate.now(), UUID.randomUUID(), originalFilename);
    }

}
