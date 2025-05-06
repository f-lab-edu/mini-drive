package dev.chan.api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsConfig implements DisposableBean {
    private final AwsProperties awsProperties;
    private S3Presigner presigner;

    @ConditionalOnProperty(name = "aws.profile", havingValue = "local")
    @Bean
    public S3Presigner LocalS3Presigner() {
        return S3Presigner.builder().region(Region.of(awsProperties.getRegion())).credentialsProvider(ProfileCredentialsProvider.create()).build();
    }

    @ConditionalOnProperty(name = "aws.profile", havingValue = "dev")
    @Bean
    public S3Presigner devS3Presigner() {
        AwsBasicCredentials credentials = loadCredentialsFromSecretManager();
        this.presigner = S3Presigner.builder().region(Region.of(awsProperties.getRegion())).credentialsProvider(StaticCredentialsProvider.create(credentials)).build();

        return presigner;
    }

    @ConditionalOnProperty(name = "aws.profile", havingValue = "prod")
    @Bean
    public S3Presigner prodS3Presigner() {
        return S3Presigner.builder().region(Region.of(awsProperties.getRegion())).credentialsProvider(DefaultCredentialsProvider.create()).build();
    }

    /**
     * Aws secretManager 에 자격증명을 요청하고, 로드하여 반환
     *
     * @return AwsCredentialSecret - accessKey, secretAccessKey 바인딩 객체
     */
    private AwsBasicCredentials loadCredentialsFromSecretManager() {
        try (SecretsManagerClient secretManager = SecretsManagerClient.builder().region(Region.of(awsProperties.getRegion())).credentialsProvider(ProfileCredentialsProvider.create(awsProperties.getProfile())).build()) {
            GetSecretValueResponse secretValue = secretManager.getSecretValue(GetSecretValueRequest.builder().secretId(awsProperties.getSecretName()).build());
            ObjectMapper mapper = new ObjectMapper();
            AwsCredentialSecret secret = mapper.readValue(secretValue.secretString(), AwsCredentialSecret.class);
            return AwsBasicCredentials.create(secret.getAccessKeyId(), secret.getSecretAccessKey());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Presigner 사용시 Aws 권장에 따른 close 처리
     */
    @Override
    public void destroy() {
        if (presigner != null) {
            presigner.close();
        }
    }
}
