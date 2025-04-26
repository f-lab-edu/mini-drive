package dev.chan.api.application.file.key;

import dev.chan.api.domain.file.FileKeySpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class FileKeyGeneratorTest {


    @Test
    @DisplayName("파일 key 생성 성공")
    void generateFileKey_success(){
        // given
        FileKeyGenerator fileKeyGenerator = new S3KeyGenerator();

        // when
        String fileKey = fileKeyGenerator.generateFileKey(new FileKeySpecification("uploads", "driveId", "test.txt"));

        // then
        assertThat(fileKey).isNotNull();
        assertThat(fileKey).startsWith("uploads/driveId/");
        assertThat(fileKey).endsWith("_test.txt");
        assertThat(fileKey).contains(LocalDate.now().toString());
    }

}
