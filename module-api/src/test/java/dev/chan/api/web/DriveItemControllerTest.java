package dev.chan.api.web;


import dev.chan.api.application.file.DriveItemService;
import dev.chan.api.web.file.DriveItemController;
import dev.chan.api.web.file.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DriveItemControllerTest {

    private MockMvc mockMvc;
    private DriveItemService driveItemService;


    @BeforeEach
    void setUp() {
        driveItemService = mock(DriveItemService.class); // Mockito mock
        DriveItemController controller = new DriveItemController(driveItemService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()) // 있다면 설정
                .build();
    }

    @Test
    @DisplayName("업로드 콜백 처리 요청을 받으고, 요청 처리 성공하면, 처리결과와 200 OK 응답을 반환한다.")
    void shouldResponseOk_WhenUploadCallback() throws Exception {
        // given
        String requestJson = """
                    {
                      "bucketName": "test-bucket",
                      "driveId": "test-drive-id",
                      "mimeType": "image/png",
                      "fileName": "test-filename",
                      "fileKey": "test-file-key",
                      "parentId": "test-parent-id"
                    }
                """;

        //when & then
        mockMvc.perform(post("/api/v1/files/upload/callback")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("필수 필드가 비어 있으면 400 BAD_REQUEST를 반환한다")
    void shouldReturnBadRequest_whenMissingRequiredFields() throws Exception {
        // given
        String requestJson = """
                    {
                      "driveId": "test-drive-id",
                    }
                """;

        //when & then
        mockMvc.perform(post("/api/v1/files/upload/callback")
                        .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
