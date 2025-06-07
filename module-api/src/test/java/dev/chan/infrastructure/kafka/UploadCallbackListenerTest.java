package dev.chan.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.application.file.DriveItemService;
import dev.chan.application.file.command.UploadCallbackCommand;
import dev.chan.infrastructure.kafka.dto.UploadCompletedEventMessage;
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
    UploadCompleteEventListener uploadCompleteEventListener;

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

        UploadCompletedEventMessage uploadCompletedEventMessage = objectMapper.readValue(message, UploadCompletedEventMessage.class);
        UploadCallbackCommand uploadCallbackCommand = uploadCompletedEventMessage.toUploadCallbackCommand();

        // when
        uploadCompleteEventListener.handleUploadCompleted(message);

        // then
        then(driveItemService).should().saveIfNotExists(uploadCallbackCommand);
    }


}