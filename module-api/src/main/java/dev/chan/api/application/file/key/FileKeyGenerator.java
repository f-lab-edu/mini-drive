package dev.chan.api.application.file.key;

import dev.chan.api.domain.file.FileKeySpecification;
import org.springframework.stereotype.Component;

public interface FileKeyGenerator {

    String generateFileKey(FileKeySpecification specification);

}
