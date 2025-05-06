package dev.chan.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@TestConfiguration
@Profile("test")
public class LocalstackS3Config {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer(){
        return new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:3")
        ).withServices(LocalStackContainer.Service.S3);
    }

    @Bean
    public S3Client testS3Client(LocalStackContainer localstack) {
        return S3Client.builder()
                .region(Region.of(localstack.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                ))
                .endpointOverride(localstack.getEndpointOverride(S3))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

    @Bean
    public S3Presigner testS3Presigner(LocalStackContainer localstack) {

        return S3Presigner.builder()
                .region(Region.of(localstack.getRegion()))
                .endpointOverride(localstack.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}


