package dev.chan.domain.file;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DriveItemService {
    public String generateThumbnailUrl(String url, String thumbnailKey) {
        return url + thumbnailKey;
    }
}
