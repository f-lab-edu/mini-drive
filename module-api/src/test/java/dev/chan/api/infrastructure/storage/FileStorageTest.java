package dev.chan.api.infrastructure.storage;


import dev.chan.api.domain.file.FileMetaData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class FileStorageTest {

    /*
    @Test
    @DisplayName("파일 메타데이터 저장_성공")
    void test_store() {
        // given
        FileKeyGenerator fileKeyGenerator = new S3KeyGenerator();

        FileStorage storage = new LocalFileStorage(new FileStorageProperties(),fileKeyGenerator);
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        FileMetaData fileMetaData = createFileMetaData();

        // when
        FileMetaData metaData = storage.store(file,"d1234");

        // then
        assertThat(metaData).isNotNull();
        assertThat(metaData.getName()).isEqualTo("test.txt");

    }*/

    public static FileMetaData createFileMetaData() {
        return FileMetaData.builder()
                .originalFileName("test.txt")
                .fileKey(UUID.randomUUID().toString())
                .size(1024)
                .build();
    }

}