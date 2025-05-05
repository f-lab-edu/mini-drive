package dev.chan.api.application.file.key;

import dev.chan.api.domain.file.FileKeySpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
public class S3KeyGenerator{

    public String generateFileKey(FileKeySpecification specification) {

        log.info("specification = {} ",specification.toString());

        return String.format("%s/%s/%s/%s_%s",
                specification.uploadPrefix(),
                specification.driveId(),
                Instant.now(),
                UUID.randomUUID(),
                specification.name());
    }

}
