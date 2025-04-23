package dev.chan.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {
    private final AwsProperties awsProperties;

    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(ProfileCredentialsProvider.builder()
                        .profileName(awsProperties.getProfile())
                        .build())
                .build();
    }

    
}
