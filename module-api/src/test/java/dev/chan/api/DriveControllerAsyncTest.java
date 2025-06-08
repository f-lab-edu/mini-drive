package dev.chan.api;

import dev.chan.application.file.PresignedUrlService;
import dev.chan.application.file.command.PresignedUrlCommand;
import dev.chan.domain.file.PresignedUrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ImportAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@Slf4j
public class DriveControllerAsyncTest {

    @Autowired
    private PresignedUrlService presignedUrlService;

    @Test
    @DisplayName("동기 vs 비동기 presignedUrl 성능 측정")
    void measurePerformance() {
        // given
        PresignedUrlCommand command = PresignedUrlCommand.of(50); // 50개 테스트

        // warm-up (JIT에 의한 영향 제거)
        for (int i = 0; i < 5; i++) {
            presignedUrlService.generateUploadUrls(command);
            presignedUrlService.generateUploadUrlsAsync(command);
        }

        // 실제 측정
        long syncStart = System.nanoTime();
        List<PresignedUrlResponse> syncResults = presignedUrlService.generateUploadUrls(command);
        long syncEnd = System.nanoTime();

        long asyncStart = System.nanoTime();
        List<PresignedUrlResponse> asyncResults = presignedUrlService.generateUploadUrlsAsync(command);
        long asyncEnd = System.nanoTime();

        long syncElapsed = (syncEnd - syncStart) / 1_000_000;
        long asyncElapsed = (asyncEnd - asyncStart) / 1_000_000;

        log.info("⏱️ 동기 방식 소요 시간: {} ms", syncElapsed);
        log.info("🚀 비동기 방식 소요 시간: {} ms", asyncElapsed);

        assertThat(syncResults).hasSize(50);
        assertThat(asyncResults).hasSize(50);
    }
}
