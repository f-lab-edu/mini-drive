package dev.chan.api.application.file;

import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PresignedUrlServiceTest {

    @Mock
    S3PresignedUrlGenerator s3PresignedUrlGenerator;

    @InjectMocks
    PresignedUrlService presignedUrlService;

    @Test
    @DisplayName("메타데이터가 유효하면 프리사인드 URL을 생성하여 반환한다.")
    void shouldCreatePresignedUrl_ifMetadataisValidate (){
        // given


        // when

        // then

    }


}