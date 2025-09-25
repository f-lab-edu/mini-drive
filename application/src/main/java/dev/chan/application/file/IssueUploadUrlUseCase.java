package dev.chan.application.file;

import dev.chan.application.dto.IssueUploadUrlCommand;
import dev.chan.application.vo.UploadTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueUploadUrlUseCase {

    /**
     * uploadTicket 을 발급해라
     *
     * @param cmd
     * @return
     */
    public UploadTicket handle(IssueUploadUrlCommand cmd) {
        /*
        // fileKey 생성해라
        String fileKey = cmd.driveId() + "/" + cmd.parentId() + "/" + cmd.fileName();
        String extension = cmd.mimeType().getExtension();
        if (extension != null && !extension.isEmpty()) {
            fileKey += "." + extension;
        }

        // presignedURL 생성해라
        String url = null;
        S3Presigner s3Presigner = S3Presigner.create();


        // 파일 키를 포함한 업로드 티켓 생성*/
        return null;
    }
}
