package dev.chan.application.file;

import dev.chan.application.CommandMother;
import dev.chan.application.dto.IssueUploadUrlCommand;
import dev.chan.application.dto.UploadTicket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class IssueUploadUrlUseCaseTest {

    @InjectMocks
    IssueUploadUrlUseCase useCase;

    // 클라이언트틑 파일 업로드를 위해 서버에 업로드 티켓을 요청한다.


    @Test
    void 업로드URL발급요청을처리하면_업로드티켓이반환된다() {
        //given
        IssueUploadUrlCommand cmd = CommandMother.IssueUploadUrlCommand();

        //when
        UploadTicket ticket = useCase.handle(cmd);

        //then
        assertNotNull(ticket);
        //assertThat(ticket.fileKey()).isEqualTo();

    }

    @Test
    void 파일업로드_단건_실패() {
        //given

        //when

        //then
    }


}
