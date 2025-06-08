package dev.chan.api.file;

import dev.chan.api.file.request.PresignedUrlRequest;
import dev.chan.api.file.request.UploadCallbackRequest;
import dev.chan.application.file.DriveItemService;
import dev.chan.application.file.PresignedUrlService;
import dev.chan.domain.file.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class DriveItemController {

    private final DriveItemService driveItemService;
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
        log.info("📩 S3 업로드 콜백 수신: {}", request);
        driveItemService.registerUploadedFile(request.toUploadCallbackCommand());

        // TODO : 업로드 콜백 응답 추가 예정입니다.
        return ResponseEntity.ok().build();
    }

    @GetMapping("/upload-url")
    public CompletableFuture<ResponseEntity<?>> uploadUrl(@RequestBody PresignedUrlRequest request) {
        return null;
    }

    @GetMapping("/upload-url/async")
    public ResponseEntity<List<PresignedUrlResponse>> uploadUrlAsync(@RequestBody PresignedUrlRequest request) {
        List<PresignedUrlResponse> presignedUrlResponses = presignedUrlService.generateUploadUrlsAsync(request.toPresignedUrlCommand());
        return ResponseEntity.ok(presignedUrlResponses);
    }

}

