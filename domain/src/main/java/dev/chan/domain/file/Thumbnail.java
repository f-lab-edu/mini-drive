package dev.chan.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Thumbnail {
    private final String thumbnailKey;

    public String getUrl(String cdnDomain) {
        return cdnDomain + "/" + thumbnailKey;
    }
}
