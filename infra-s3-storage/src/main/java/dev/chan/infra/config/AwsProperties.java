package dev.chan.infra.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
@Getter
@RequiredArgsConstructor
public class AwsProperties {

    @NotNull
    private final String region;

    @NotNull
    private final String profile;

    @NotNull
    private final S3 s3;

    @NotNull
    private final Secret secret;

    @Getter
    @RequiredArgsConstructor
    public static class Secret {
        private final String fileName;
    }

    public String getSecretName() {
        return secret.getFileName();
    }

    @Getter
    @RequiredArgsConstructor
    public static class S3 {
        private final String bucket;
        private final String uploadPrefix;
    }


    public String getBucketName() {
        return s3.getBucket();
    }

    public String getUploadPrefix() {
        return s3.getUploadPrefix();
    }

}
