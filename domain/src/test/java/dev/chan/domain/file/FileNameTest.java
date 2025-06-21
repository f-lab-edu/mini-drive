package dev.chan.domain.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileNameTest {


    @Test
    @DisplayName("올바른 파일이름을 받으면 확장자를 리턴한다.")
    void fileNameTest_success() {
        //given
        FileName fileName = new FileName("test.txt");

        //when
        String expected = fileName.getExtension();

        //then
        assertThat(expected).isEqualTo("txt");
    }

    @Test
    @DisplayName("올바르지 않은 파일이름을 받으면 공백문자를 리턴한다.")
    void fileName_whenLastIndex() {
        //given
        FileName fileName = new FileName("testtxt.");

        //when
        String expected = fileName.getExtension();

        //then
        assertThat(expected).isEqualTo("");
    }

}