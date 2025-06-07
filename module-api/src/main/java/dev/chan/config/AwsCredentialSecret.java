package dev.chan.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor
public class AwsCredentialSecret {

    @NotBlank
    @JsonProperty("aws_access_key_id")
    private String accessKeyId;

    @NotBlank
    @JsonProperty("aws_secret_access_key")
    private String secretAccessKey;
}
