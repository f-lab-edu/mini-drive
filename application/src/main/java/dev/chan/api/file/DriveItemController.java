package dev.chan.api.file;

import dev.chan.api.file.request.PresignedUrlRequest;
import dev.chan.api.file.request.UploadCallbackRequest;
import dev.chan.application.file.DriveItemAppService;
import dev.chan.application.file.PresignedUrlResponse;
import dev.chan.application.file.PresignedUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class DriveItemController {

    private final DriveItemAppService driveItemAppService;
    private final PresignedUrlService presignedUrlService;

    /**
     * S3 업로드 콜백을 처리하는 엔드포인트
     * 클라이언트에서 S3 업로드가 완료된 후 이 엔드포인트로 콜백 요청을 보냅니다.
     *
     * @param request 업로드 콜백 요청
     * @return HTTP 200 OK 응답
     */
    @PostMapping("/upload/callback")
    public ResponseEntity<Void> handle(@RequestBody UploadCallbackRequest request) {
        log.info("📩 S3 업로드 콜백         driveItemService.registerUploadedFile(request.toCommand());\n수신: {}", request);

        return ResponseEntity.ok().build();
    }

    /**
     * 비동기 방식으로 presigned URL을 생성하는 엔드포인트
     * 클라이언트에서 요청 시, 비동기로 presigned URL을 생성하여 응답합니다.
     *
     * @param request presigned URL 요청 정보
     * @return 생성된 presigned URL 리스트
     */
    @GetMapping("/upload-url")
    public ResponseEntity<List<PresignedUrlResponse>> uploadUrlAsync(@RequestBody PresignedUrlRequest request) {
        List<PresignedUrlResponse> presignedUrlResponses = presignedUrlService.generateUploadUrlsAsync(request.toPresignedUrlCommand());
        return ResponseEntity.ok(presignedUrlResponses);
    }

}

