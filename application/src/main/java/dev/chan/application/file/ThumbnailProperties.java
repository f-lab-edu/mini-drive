package dev.chan.application.file;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "thumbnail")
public class ThumbnailProperties {
    private String domain;


}
