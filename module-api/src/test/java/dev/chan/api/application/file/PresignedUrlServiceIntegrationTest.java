package dev.chan.api.application.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PresignedUrlServiceIntegrationTest {

    @Autowired
    private S3PresignedUrlGenerator urlGenerator;

    @Autowired
    private PresignedUrlService presignedUrlService;

    @Test
    @DisplayName("PresignedUrlService가 파일 메타데이터와 멀티파트 파일로 presigned URL들을 생성한다")
    void shouldGenerattePresignedUrl() {
        // given
        String driveId = "d1234";
        PresignedUrlCommand presignedUrlCommand = createPresignedUrlCommand();

        // when
        List<String> presignedUrls = presignedUrlService.generateUploadUrls(presignedUrlCommand);

        // then
        assertThat(presignedUrls)
                .isNotNull()
                .hasSize(1);

        String generatedUrl = presignedUrls.getFirst();
        assertThat(generatedUrl)
                .contains(LocalDate.now().toString())
                .contains(driveId);
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
                .parentId("")
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