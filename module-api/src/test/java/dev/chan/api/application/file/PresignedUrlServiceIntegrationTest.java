package dev.chan.api.application.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.domain.file.PresignedUrlResponse;
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

import static dev.chan.api.application.file.PresignedUrlServiceTest.metaDataDto;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(properties = "aws.profile=dev")
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
        List<PresignedUrlResponse> responses = presignedUrlService.generateUploadUrls(presignedUrlCommand);

        // then
        assertThat(responses).isNotNull().hasSize(1);

        PresignedUrlResponse first = responses.getFirst();
        assertThat(first.url()).contains(LocalDate.now().toString()).contains(driveId);
    }

    public PresignedUrlCommand createPresignedUrlCommand() {
        FileMetaDataDto metaDataDto = metaDataDto();
        return PresignedUrlCommand.builder().driveId("d1234").parentId("").fileMetaDataDtoList(List.of(metaDataDto)).build();
    }
}