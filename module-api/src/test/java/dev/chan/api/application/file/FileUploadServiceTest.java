package dev.chan.api.application.file;


import dev.chan.api.application.file.command.UploadCommand;
import dev.chan.api.domain.file.FileMetaData;
import dev.chan.api.infrastructure.storage.LocalFileStorage;
import dev.chan.api.web.file.request.FileMetaDataDTO;
import dev.chan.api.web.file.response.ApiResponse;
import dev.chan.api.web.file.response.FileUploadResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static dev.chan.api.infrastructure.storage.FileStorageTest.createFileMetaData;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    @InjectMocks
    FileUploadServiceImpl fileUploadService;

    @Mock
    LocalFileStorage fileStorage;

    @Test
    @DisplayName("파일업로드_성공")
    void upload_success(){
        // given
        String driveId = "drive1";
        String parentId = "root";

        List<MultipartFile> files = List.of(new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes()));
        List <FileMetaData> metaData = List.of(createFileMetaData());
        doReturn(metaData).when(fileStorage).storeAll(files);

        // when
        UploadCommand uploadCommand = getUploadCommand(driveId, parentId, files);
        FileUploadResponse uploaded = fileUploadService.upload(uploadCommand);

        // then
        assertThat(uploaded).isNotNull();
        assertThat(uploaded.getMetaDataList()).hasSize(1);
        assertThat(uploaded.getMetaDataList().getFirst().getName()).isEqualTo("test.txt");
    }


    private UploadCommand getUploadCommand(String driveId, String parentId, List<MultipartFile> files) {
        FileMetaDataDTO fileMetaDataDto = new FileMetaDataDTO();
        fileMetaDataDto.setMimeType("text/plain");
        List<FileMetaDataDTO> entries = List.of(fileMetaDataDto);
        return new UploadCommand(driveId, parentId,files,entries);
    }


    @Test
    @DisplayName("")
    void uploadFile(){
        // given
        
        // when 
        
        // then 
    }

    @DisplayName("파일 업로드_성공")
    @Test
    void storeFileMetaData_success(){
        String fileId = "f1234";
        String fileName = "test";
        String mimeType = "image/jpeg";
        int fileSize = 10;
        


        // given

        // when

        // then

    }

    @Test
    @DisplayName("파일 업로드_실패")
    void storeFileMetaData_fail(){
        // given

        // when

        // then
    }

    @Test
    void uploadService_isNotNull() {
        assertThat(fileUploadService).isNotNull();
    }


    @DisplayName("용량 초과 시 업로드 실패 예외가 발생한다.")
    @Test
    void uploadService_storage_exceedException(){
        // given

        // when

        // then
    }


  
}