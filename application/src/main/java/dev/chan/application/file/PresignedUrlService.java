package dev.chan.application.file;

import dev.chan.application.command.PresignedUrlCommand;
import dev.chan.application.command.PresignedUrlSpecification;
import dev.chan.common.util.ThreadPoolLogger;
import dev.chan.domain.FileKeySpecification;
import dev.chan.infra.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final S3KeyGenerator keyGenerator;
    private final S3PresignedUrlGenerator urlGenerator;
    private final AwsProperties properties;


    private final ThreadPoolTaskExecutor presignedUrlExecutor;

    /**
     * presignedUrl을 생성해 List로 반환합니다.
     *
     * @param presignedUrlCommand
     * @return List<presingedUrlResponse> - presignedUrl을 포함한 클라이언트에 필요한 응답 데이터 리스트
     */
    public List<PresignedUrlResponse> generateUploadUrls(PresignedUrlCommand presignedUrlCommand) {
        log.info("[Sync] {}", presignedUrlCommand);

        return presignedUrlCommand.getFileMetaDataDtoList().stream().map(metaDto -> {
            FileKeySpecification keySpec = FileKeySpecification.toKeySpec(presignedUrlCommand.getDriveId(), metaDto.getName(), properties.getUploadPrefix());

            //log.info("keySpecification={}", keySpec);

            String key = keyGenerator.generateFileKey(keySpec);

            // log.info("key={}", key);

            PresignedUrlSpecification urlSpec = PresignedUrlSpecification
                    .toUrlSpec(properties.getBucketName(), presignedUrlCommand, key, metaDto.toMetadata());

            //log.info("UrlSpecification={}", urlSpec);

            return urlGenerator.createPresignedUrl(urlSpec);
        }).toList();

    }

    /**
     * 비동기 방식으로 presignedUrl을 생성해 List로 반환합니다.
     *
     * @param presignedUrlCommand
     * @return
     */
    public List<PresignedUrlResponse> generateUploadUrlsAsync(PresignedUrlCommand presignedUrlCommand) {
        List<CompletableFuture<PresignedUrlResponse>> futures = presignedUrlCommand.getFileMetaDataDtoList().stream()
                .map(metaDto -> CompletableFuture.supplyAsync(() -> {
                    ThreadPoolLogger.logExecutorStats("presignedUrlExecutor", presignedUrlExecutor);

                    FileKeySpecification keySpec = FileKeySpecification.toKeySpec(presignedUrlCommand.getDriveId(), metaDto.getName(), properties.getUploadPrefix());

                    String key = keyGenerator.generateFileKey(keySpec);

                    PresignedUrlSpecification urlSpec = PresignedUrlSpecification.toUrlSpec(properties.getBucketName(), presignedUrlCommand, key, metaDto.toMetadata());

                    return urlGenerator.createPresignedUrl(urlSpec);
                }, presignedUrlExecutor).exceptionally(ex -> {
                    log.error("비동기 Presigned URL 생성 중 오류 발생: {}", metaDto.getName(), ex);
                    throw new RuntimeException("presignedUrl 생성 중 오류 발생", ex); // 예외 발생 시 null 반환
                })).toList();

        //스레드 풀 상태 로깅
        ThreadPoolLogger.logExecutorStats("presignedUrlExecutor", presignedUrlExecutor);

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();

    }
}