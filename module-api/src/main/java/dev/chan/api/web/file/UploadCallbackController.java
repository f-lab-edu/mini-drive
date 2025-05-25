package dev.chan.api.web.file;

import dev.chan.api.web.file.request.UploadCallbackRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/files/upload")
public class UploadCallbackController {

    @PostMapping("/callback")
    public ResponseEntity<Void> handle(@RequestBody UploadCallbackRequest request){
        log.info("📩 S3 업로드 콜백 수신: {}", request);
        return ResponseEntity.ok().build();
    }

}

