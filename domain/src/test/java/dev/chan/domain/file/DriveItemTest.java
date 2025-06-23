package dev.chan.domain.file;

import dev.chan.domain.exception.MaxUploadSizeExceededException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DriveItemTest {


    @Test
    @DisplayName("driveId와 파일Id 로 파일 키를 생성할 수 있다.")
    void generateFileKey_success() {
        //given
        String driveId = "drive123";
        String id = "f1234";

        //when

        //then
    }

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

    @Test
    @DisplayName("업로드된 파일의 bytes를 받으면, readableSize() 호출 시 용량에 해당하는 바이트 단위를 리턴한다.")
    void readableSize_success() {
        // given
        long GB = 1024 * 1024 * 1024;
        long MB = 1024 * 1024;
        long KB = 1024;

        FileSize sizeGb = new FileSize(2 * GB);
        FileSize sizeMb = new FileSize(2 * MB);
        FileSize sizeKb = new FileSize(2 * KB);
        FileSize sizeB = new FileSize(2);

        // when
        // then
        assertThat(sizeGb.readableSize()).contains("GB");
        assertThat(sizeMb.readableSize()).contains("MB");
        assertThat(sizeKb.readableSize()).contains("KB");
        assertThat(sizeB.readableSize()).contains("B");

    }


}