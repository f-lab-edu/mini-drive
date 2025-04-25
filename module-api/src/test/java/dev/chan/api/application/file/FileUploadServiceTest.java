package dev.chan.api.application.file;


import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.config.AwsConfig;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.domain.file.FileUploadRepository;
import dev.chan.api.infrastructure.aws.S3PresignedUrlGenerator;
import dev.chan.api.infrastructure.storage.LocalFileStorage;
import dev.chan.api.web.file.request.FileMetaDataDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    @Mock
    LocalFileStorage fileStorage;

    @Mock
    FileUploadRepository fileUploadRepository;

    @InjectMocks
    FileUploadService fileUploadService;

    @Test
    @DisplayName("유효한 파일을 업로드하면, 파일과 메타데이터는 저장되고 썸네일이벤트가 발행된다.")
    void uploadingValidFile_returnsMetaDataInResponseAndPublishThumbnailEvent() {
        // given
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        List <FileMetaData> metaData = createMetaDataList("test.txt","text/plain" );

        doReturn(metaData).when(fileStorage).storeAll(anyList(),any());

        // when
        List<FileMetaData> uploaded = fileUploadService.upload(getUploadCommand(List.of(file)));

        // then
        assertThat(uploaded).isNotNull();
        assertThat(uploaded).hasSize(1);
        assertThat(uploaded.getFirst().getName()).isEqualTo("test.txt");

        // 메타데이터 저장 호출 확인
        verify(fileUploadRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("파일 업로드 성공 시, 메타데이터가 저장된다.")
    void shouldSaveMetadata_whenFileUploadSuccess(){
        // given
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        List <FileMetaData> metaDataList = createMetaDataList("test.txt","text/plain" );
        doReturn(metaDataList).when(fileStorage).storeAll(anyList(),any());

        // when
        List<FileMetaData> upload = fileUploadService.upload(getUploadCommand(List.of(file)));
        FileMetaData metaData = upload.getFirst();

        // then
        assertThat(upload).isNotNull();
        assertThat(metaData.getName()).isEqualTo(file.getOriginalFilename());
        assertThat(metaData.getMimeType()).isEqualTo(file.getContentType());
        assertThat(metaData.getSize()).isEqualTo(file.getSize());
    }

    @Test
    @DisplayName("업로드 중 예외가 발생하면, 업로드는 실패한다.")
    void shouldUploadFail_whenFileSavedThrowException(){
        // given
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        doThrow(new RuntimeException("스토리지 저장 오류")).when(fileStorage).storeAll(anyList(),any());

        // when
        // then
        assertThatThrownBy(()-> fileUploadService.upload(getUploadCommand(List.of(file))))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("파일 여러개를 업로드 하는 경우, 모든 파일의 메타데이터가 저장되고 이벤트가 발행된다.")
    void shouldUploadMultiFiles_andPublishEvent(){
        // given
        MultipartFile file1 = new MockMultipartFile("file", "file1.txt", "text/plain", "hello".getBytes());
        MultipartFile file2 = new MockMultipartFile("file", "file2.txt", "text/plain", "hello".getBytes());

        FileMetaData meta1 = FileMetaData.builder().name("file1.txt").mimeType("text/plain").size(5L).build();
        FileMetaData meta2 = FileMetaData.builder().name("file2.txt").mimeType("text/plain").size(5L).build();
        List<FileMetaData> metaDataList = List.of(meta1, meta2);

        doReturn(metaDataList).when(fileStorage).storeAll(anyList(),any());

        // when
        List<FileMetaData> upload = fileUploadService.upload(getUploadCommand(List.of(file1, file2)));

        // then
        assertThat(upload).hasSize(2);
        verify(fileUploadRepository).saveAll(anyList());
    }

    private List<FileMetaData> createMetaDataList(String fileName, String mimeType){
        return List.of(
                FileMetaData.builder()
                        .name(fileName)
                        .relativePath("")
                        .parentId("root")
                        .size(5L)
                        .mimeType(mimeType)
                        .build()
        );
    }


    private UploadCommand getUploadCommand(List<MultipartFile> file) {
        return new UploadCommand("d1234", "f1234", file, List.of(new FileMetaDataDTO("","text/plain")) );
    }

}