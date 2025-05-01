package dev.chan.api.application.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsProperties;
import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresignedUrlServiceTest {

    @Mock
    S3PresignedUrlGenerator urlGenerator;

    @Mock
    AwsProperties properties;

    @Mock
    S3KeyGenerator keyGenerator;

    @InjectMocks
    PresignedUrlService presignedUrlService;

    @Test
    @DisplayName("메타데이터가 유효하면 프리사인드 URL을 ")
    void shouldCreateValidPresignedUrl() {
        // given
        String bucketName = "test-bucket";
        String fileKey = "test-file.txt";

        doReturn(bucketName).when(properties).getBucketName();
        doReturn("uploads").when(properties).getUploadPrefix();
        doReturn("uploads/test-file.txt").when(keyGenerator).generateFileKey(any());
        doReturn("https://" + bucketName + ".s3.amazonaws.com/" + fileKey).when(urlGenerator).createPresignedUrl(any());

        // when
        List<String> presignedUrl = presignedUrlService.generateUploadUrls(createPresignedUrlCommand());

        // then
        assertThat(presignedUrl).hasSize(1);
        assertThat(presignedUrl).contains("https://" + bucketName + ".s3.amazonaws.com/" + fileKey);

        verify(keyGenerator, times(1)).generateFileKey(any());
        verify(urlGenerator, times(1)).createPresignedUrl(any());
    }

    private PresignedUrlCommand createPresignedUrlCommand() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "file-content".getBytes()
        );

        FileMetaDataDto metaDataDto = new FileMetaDataDto("test.txt", "text/plain");
        return PresignedUrlCommand.builder()
                .driveId("d1234")
                .fileMetaDataDtoList(List.of(metaDataDto))
                .multipartFiles(List.of(multipartFile))
                .build();
    }

    @Getter
    @Setter
    private static class PresignedUrlRequest {
        private String driveId;
        private String mimeType;
        private String relativePath;
        private MultipartFile file;

        public static PresignedUrlCommand toCommand(PresignedUrlRequest presignedRequest) {
            return PresignedUrlCommand.builder()
                    .driveId(presignedRequest.getDriveId())
                    .build();
        }
    }
}