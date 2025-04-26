package dev.chan.api.application.file;

import dev.chan.api.domain.file.PresignedUrlSpecification;

public interface PresignedUrlGenerator {

    String createPresignedUrl(PresignedUrlSpecification specification);
}
