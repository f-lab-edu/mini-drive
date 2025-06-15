package dev.chan.domain.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.common.MimeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MimeTypeTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("문자열 확장자가 주어지면, 확장자에 대응하는 마임타입을 리턴한다.")
    void shouldReturnMimeType_WhenStringExtension() {
        //given/when/then
        assertThat(MimeType.from("image/png")).isEqualTo(MimeType.PNG);
    }

    @Test
    @DisplayName("mimeType이 포함된 JSON이 주어졌을 때, ObjectMapper로 역직렬화하면, MimeType enum으로 매핑된다")
    void givenJsonWithMimeType_whenDeserialized_thenReturnsCorrespondingEnum() throws JsonProcessingException {
        //given
        String json = "{\"mimeType\":\"image/png\"}";

        //when
        MimeTypeFixture result = objectMapper.readValue(json, MimeTypeFixture.class);

        //then
        assertThat(result.getMimeType()).isEqualTo(MimeType.PNG);
    }

    @Test
    @DisplayName("mimeType을 가진 객체가 주어졌을 때, ObjectMapper 로 직렬화면, 해당 객체를 JSON 형태의 문자열을 리턴한다.")
    void givenMimeTypeEnum_whenSerialized_thenReturnsMimeTypeString() throws JsonProcessingException {

        //given
        MimeTypeFixture expected = new MimeTypeFixture(MimeType.FOLDER);

        //when
        String result = objectMapper.writeValueAsString(expected);

        //then
        assertThat(result).isEqualTo("{\"mimeType\":\"" + expected.getMimeType().getMime() + "\"}");
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MimeTypeFixture {
        private MimeType mimeType;
    }
}