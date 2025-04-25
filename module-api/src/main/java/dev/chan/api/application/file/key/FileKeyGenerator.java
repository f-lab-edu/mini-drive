package dev.chan.api.application.file.key;

import org.springframework.stereotype.Component;

public interface FileKeyGenerator {

    String generateFileKey(String baseDir, String fileName, String originalFilename);

}
