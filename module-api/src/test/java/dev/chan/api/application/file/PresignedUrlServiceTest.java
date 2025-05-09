package dev.chan.api.application.file;

import dev.chan.api.application.file.command.PresignedUrlCommand;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsProperties;
import dev.chan.api.domain.file.PresignedUrlResponse;
import dev.chan.api.domain.file.PresignedUrlSpecification;
import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import dev.chan.api.web.file.request.FileMetaDataDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
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
    @DisplayName("유효한 메타데이터를 받으면, generateUrl을 생성한다.")
    void shouldGenerateUrl_whenMetaDataIsValid(){
        //given
        PresignedUrlCommand command = presignedUrlCommand();
        FileMetaDataDto metadata = command.getFileMetaDataDtoList().getFirst();
        doReturn("upload").when(properties).getUploadPrefix();
        doReturn("fileKey").when(keyGenerator).generateFileKey(any());
        doReturn(presignedUrlResponse()).when(urlGenerator).createPresignedUrl(any());

        log.info("command = {}",command);

        //when
        List<PresignedUrlResponse> response  = presignedUrlService.generateUploadUrls(command);

        log.info("response = {}",response);

        //then
        assertThat(response).hasSize(1);

        PresignedUrlResponse first = response.getFirst();
        assertThat(first.driveId()).isEqualTo(command.getDriveId());
        assertThat(first.parentId()).isEqualTo(command.getParentId());
        assertThat(first.mimeType()).isEqualTo(metadata.getMimeType());
        assertThat(first.size()).isEqualTo(metadata.getSize());

        verify(keyGenerator, times(1)).generateFileKey(any());
        verify(urlGenerator, times(1)).createPresignedUrl(any());
    }


    public PresignedUrlSpecification presignedUrlSpecification(){
        return new PresignedUrlSpecification("upload/",
                presignedUrlCommand().getDriveId(),
                presignedUrlCommand().getParentId(),
                "fileKey",
                metaDataDto().toMetadata());
    }

    public PresignedUrlResponse presignedUrlResponse(){
        return PresignedUrlResponse.from(presignedUrlSpecification(),"url", Instant.now());
    }

    public static FileMetaDataDto metaDataDto(){
        return FileMetaDataDto.builder()
                .mimeType("text/plain")
                .name("test.txt")
                .size(10L)
                .build();
    }

    public PresignedUrlCommand presignedUrlCommand(){
        return PresignedUrlCommand.builder()
                .driveId("drive")
                .parentId("parent")
                .fileMetaDataDtoList(List.of(metaDataDto()))
                .build();
    }
}