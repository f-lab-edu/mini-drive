package dev.chan.application.file;

import dev.chan.application.command.FolderCreateCommand;
import dev.chan.application.command.UploadCallbackCommand;
import dev.chan.application.exception.DriveItemNotFoundException;
import dev.chan.application.vo.UploadCallbackResult;
import dev.chan.domain.UploadedFileRegisteredEvent;
import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemEventPublisher;
import dev.chan.domain.file.DriveItemFactory;
import dev.chan.domain.file.DriveItemRepository;
import dev.chan.domain.userstate.UserItemState;
import dev.chan.domain.userstate.UserItemStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class DriveItemAppService {

    private final DriveItemRepository driveItemRepository;
    private final UserItemStateRepository userItemStaterepository;
    private final DriveItemEventPublisher domainEventPublisher;

    /**
     * 업로드된 파일 데이터 등록
     * presignedURL 생성시 미리 생성된 fileItem 정보를 repository 에 저장합니다.
     *
     * @param command
     * @return
     */
    public UploadCallbackResult registerUploadedFile(UploadCallbackCommand command) {
        log.info("[registerUploadedFile()] {}", command);
        // Parent 조회. 없으면 ROOT 를 PARENT 로 등록
        DriveItem parent = driveItemRepository.findById(command.parentId())
                .orElseGet(() -> DriveItem.ofRoot(command.driveId()));

        DriveItem newItem = DriveItemFactory.createFrom(
                command.driveId(),
                parent,
                command.fileId(),
                command.mimeType(),
                command.size(),
                command.fileName(),
                command.userId()
        );

        if (newItem.getMimeType().isThumbnailSupported()) {
            //TODO 썸네일 생성정책에 의한 썸네일생성

        }

        DriveItem savedItem = driveItemRepository.save(newItem);


        // 파일 아이템 생성 완료 이벤트 등록
        domainEventPublisher.publish(
                new UploadedFileRegisteredEvent(savedItem.getDriveId(),
                        savedItem.getParentId(),
                        savedItem.getIdToString())
        );

        // user 별 item 상태 등록
        UserItemState userItemState = UserItemState.create(savedItem.getId(), savedItem.getCreatedBy());
        UserItemState savedState = userItemStaterepository.save(userItemState);

        // 파일 아이템 상태 등록
        return new UploadCallbackResult(savedItem, savedState);
    }

    /**
     * 폴더를 생성합니다.
     *
     * @param command
     * @return 생성된 폴더 아이템
     */
    public DriveItem createFolder(FolderCreateCommand command) {
        log.info("[createFolder()] {}", command);

        DriveItem item = DriveItemFactory.createFolder(
                command.driveId(),
                command.mimeType(),
                command.fileName());

        driveItemRepository.save(item);
        return driveItemRepository.findById(item.getIdToString())
                .orElseThrow(() -> new DriveItemNotFoundException(item.getIdToString()));
    }

    // 썸네일 생성전 클라이언트 응답을 위한 편의성 메서드
    public String thumbnailUrl(DriveItem item) {
        return null; // domain + "/" + item.thumbnailKey();
    }
    /**
     * 업로드된 파일이 존재하지 않을 경우, 파일을 등록합니다.
     *
     * @param uploadCallbackCommand

    public void registerUploadedFileIfNotExists(UploadCallbackCommand uploadCallbackCommand) {
    log.info("[registerUploadedFileIfNotExists()] {}", uploadCallbackCommand);

    driveItemRepository.findByFileKey(uploadCallbackCommand.fileKey())
    .orElseGet(() -> registerUploadedFile(uploadCallbackCommand));
    }*/
}


