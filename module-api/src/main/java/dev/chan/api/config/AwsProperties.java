package dev.chan.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
@Getter
@RequiredArgsConstructor
public class AwsProperties implements FileStorageProperties {

    private final String region;
    private final String profile;
    private final S3 s3;

    @Getter
    @RequiredArgsConstructor
    public static class S3 {
        private final String bucket;
        private final String uploadPrefix;
    }

    @Override
    public String getBucketName(){
        if (s3 == null) {
            throw new IllegalStateException("AWS S3 properties are not initialized properly.");
        }
        return s3.getBucket();
    }

    @Override
    public String getUploadPrefix(){
        return s3.getUploadPrefix();
    }

}
