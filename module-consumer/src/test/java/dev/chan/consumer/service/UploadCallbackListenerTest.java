package dev.chan.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.application.file.DriveItemService;
import dev.chan.application.file.command.UploadCallbackCommand;
import dev.chan.consumer.dto.UploadCompletedEvent;
import dev.chan.consumer.listener.UploadCompletedListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UploadCallbackListenerTest {

    @Mock
    DriveItemService driveItemService;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    UploadCompletedListener uploadCompleteEventListener;

    @DisplayName("업로드 완료 메시지가 주어지면, DriveItemService 의 registerUploadedFile 메서드를 호출한다")
    @Test
    void handleUploadCompleted_ShouldCallRegisterUploadedFile() throws JsonProcessingException {
        // given
        String message = """
                {
                  "bucketName": "test-bucket",
                  "fileName": "test-file.txt",
                  "driveId": "test-drive-id",
                  "parentId": "test-parent-id",
                  "fileKey": "test-file-key",
                  "mimeType": "text/plain",
                  "size": 12345
                }
                """;

        UploadCompletedEvent uploadCompletedEventMessage = objectMapper.readValue(message, UploadCompletedEvent.class);
        UploadCallbackCommand uploadCallbackCommand = uploadCompletedEventMessage.toCommand();

        // when

        // then
        then(driveItemService).should().registerUploadedFileIfNotExists(uploadCallbackCommand);
    }


}