package dev.chan.domain.file;

import dev.chan.common.MimeType;

public class ThumbnailGenerationPolicy {
    public boolean supports(MimeType mimeType) {
        return mimeType.isThumbnailSupported();
    }
}
