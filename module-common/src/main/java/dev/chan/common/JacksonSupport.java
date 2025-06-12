package dev.chan.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonSupport {

    /**
     * 기본 Jackson ObjectMapper를 생성합니다.
     * Java 8 날짜/시간 API를 지원하는 모듈을 등록하고, 날짜를 타임스탬프로 직렬화하지 않도록 설정합니다.
     *
     * @return 기본 ObjectMapper
     */
    public static ObjectMapper basicMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
