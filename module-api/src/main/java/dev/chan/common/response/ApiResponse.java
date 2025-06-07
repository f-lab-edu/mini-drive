package dev.chan.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiResponse {

    private boolean isError;
    private String code;
    private HttpStatus status;
    private String message;
    private Object data;

    private LocalDateTime timestamp = LocalDateTime.now();

    private ApiResponse(String code, String message, Object data) {
    }

}
