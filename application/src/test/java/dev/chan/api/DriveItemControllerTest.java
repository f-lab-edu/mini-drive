package dev.chan.api;


import dev.chan.api.file.DriveItemController;
import dev.chan.application.file.DriveItemService;
import dev.chan.application.file.PresignedUrlService;
import dev.chan.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DriveItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PresignedUrlService presignedUrlService;

    @Mock
    private DriveItemService driveItemService;

    @InjectMocks
    DriveItemController driveItemController;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(driveItemController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 있다면 설정
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

    /*
    @Test
    @DisplayName("Presigned URL 요청에 대해 CompletableFuture로 응답한다")
    void shouldReturnPresignedUrl_whenValidRequest() throws Exception {
        //given
        String content = """
                {
                  "driveId": "test-drive-id",
                  "parentId": "test-parent-id",
                  "fileMetaDataDtoList": [
                    {
                      "name": "test-file.txt",
                      "size": 12345,
                      "mimeType": "text/plain"
                    }
                  ]
                }
                """;
        List<PresignedUrlResponse> urls = List.of(
                new PresignedUrlResponse("drive-id", "parent-id", "text/plain", "test-file.txt", 20L, MimeType.TEXT, "url", null)
        );

        //when


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/files/upload/async")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(request().asyncStarted())
                .andReturn();

        //then
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

    }*/

}
