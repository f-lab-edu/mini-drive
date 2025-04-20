package dev.chan.api.infrastructure.storage;


import dev.chan.api.application.file.FileStorage;
import dev.chan.api.application.file.key.S3KeyGenerator;
import dev.chan.api.application.file.key.FileKeyGenerator;
import dev.chan.api.domain.file.FileMetaData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class FileStorageTest {


    @Test
    @DisplayName("파일 메타데이터 저장_성공")
    void test_store() {
        // given
        FileKeyGenerator fileKeyGenerator = new S3KeyGenerator();
        FileStorage storage = new LocalFileStorage("uploads",fileKeyGenerator);
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        FileMetaData fileMetaData = createFileMetaData();

        // when
        FileMetaData metaData = storage.store(file,"d1234");

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