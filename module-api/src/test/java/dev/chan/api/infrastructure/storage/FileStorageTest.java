package dev.chan.api.infrastructure.storage;


import dev.chan.api.application.file.FileStorage;
import dev.chan.api.domain.file.FileMetaData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class FileStorageTest {

    @Mock
    FileStorage fileStorage;

    @Test
    @DisplayName("파일 메타데이터 저장_성공")
    void test_store(){
        // given
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        FileMetaData fileMetaData = createFileMetaData();
        Mockito.doReturn(fileMetaData).when(fileStorage).store(file);

        // when
        FileMetaData metaData = fileStorage.store(file);

        // then
        assertThat(metaData).isNotNull();
        assertThat(metaData.getName()).isEqualTo("test.txt");

    }

    public static FileMetaData createFileMetaData(){
        return FileMetaData.builder()
                .name("test.txt")
                .fileKey(UUID.randomUUID().toString())
                .size(1024)
                .build();
    }

}