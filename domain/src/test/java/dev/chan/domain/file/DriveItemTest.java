package dev.chan.domain.file;

import dev.chan.domain.exception.MaxUploadSizeExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DriveItemTest {


    @Test
    @DisplayName("지정한 최대 용량보다 보다 용량이 큰 파일이 업로드 되면, MaxUploadSizeExceededException throw 한다.")
    void fileSizeInit_fail() {
        //given
        long size = 1024 * 1024 * 1024 * 3L;

        //when
        //then
        assertThatThrownBy(() -> new FileSize(size))
                .isInstanceOf(MaxUploadSizeExceededException.class);

    }


}