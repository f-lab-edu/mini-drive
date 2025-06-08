package dev.chan.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {

    private boolean isError;
    private String code;
    private HttpStatus status;
    private String message;
    private T data;

    //private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .isError(false)
                .code("200")
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .isError(true)
                .code(code)
                .status(HttpStatus.BAD_REQUEST)
                .message(message)
                .data(null)
                .build();
    }

}
