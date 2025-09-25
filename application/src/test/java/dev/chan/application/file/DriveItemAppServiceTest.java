package dev.chan.application.file;


import dev.chan.application.command.FolderCreateCommand;
import dev.chan.application.command.UploadCallbackCommand;
import dev.chan.application.vo.UploadCallbackResult;
import dev.chan.common.MimeType;
import dev.chan.domain.UploadedFileRegisteredEvent;
import dev.chan.domain.file.*;
import dev.chan.domain.userstate.UserItemState;
import dev.chan.infrastructure.DriveItemMemoryRepository;
import dev.chan.infrastructure.UserItemStateMemoryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class DriveItemAppServiceTest {

    @Mock
    DriveItemEventPublisher domainEventPublisher;

    @Mock
    DriveItemMemoryRepository driveItemRepository;

    @Mock
    ThumbnailProperties thumbnailProperties;

    @Mock
    ThumbnailGenerationPolicy thumbnailPolicy;


    @Mock
    UserItemStateMemoryRepositoryImpl userItemStateRepository;

    @InjectMocks
    DriveItemAppService driveItemAppService;

    @Test
    @DisplayName("업로드 콜백 처리 명령를 받으면 DB에 메타데이터를 저장한다. ")
    void shouldSaveMetadata_WhenUploadCallbackReceived() {
        // given
        UploadCallbackCommand command = uploadCallbackCommand();

        DriveItem parent = rootFolder();
        DriveItem savedItem = DriveItemFactory.createFrom(
                command.driveId(),
                parent,
                command.fileId(),
                command.mimeType(),
                command.size(),
                command.fileName(),
                command.userId()
        );

        // 썸네일 cdn 도메인
        String domain = "https://cdn.mini-drive.dev/";

        doReturn(Optional.of(parent)).when(driveItemRepository).findById(any());
        doReturn(savedItem).when(driveItemRepository).save(any());
        doReturn(domain).when(thumbnailProperties).getDomain();

        ArgumentCaptor<DriveItem> itemCaptor = ArgumentCaptor.forClass(DriveItem.class);
        ArgumentCaptor<UploadedFileRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UploadedFileRegisteredEvent.class);
        // ArgumentCaptor<ThumbnailItemPolicy> thumbnailPolicyCaptor = ArgumentCaptor.forClass(ThumbnailItemPolicy.class);

        // when
        UploadCallbackResult result = driveItemAppService.registerUploadedFile(command);

        // then
        verify(driveItemRepository, times(1)).save(itemCaptor.capture());
        verify(domainEventPublisher, times(1)).publish(eventCaptor.capture());
        verify(thumbnailPolicy, times(1)).supports(savedItem.getMetadata().getMimeType());

        /*== save 함수 호출시 argument 검증 ==*/
        DriveItem actualItem = itemCaptor.getValue();
        assertThat(actualItem).isNotNull();
        assertThat(actualItem.getFileName()).isEqualTo(command.fileName());
        assertThat(actualItem.getMimeType().getMime()).isEqualTo(command.mimeType());
        assertThat(actualItem.getSize()).isEqualTo(command.size());
        assertThat(actualItem.getParent()).isEqualTo(parent);

        /*== event 생성시 전달되는 argument 검증 ==*/
        UploadedFileRegisteredEvent actualEvent = eventCaptor.getValue();
        assertThat(actualEvent).isNotNull();
        assertThat(actualEvent.getDriveId()).isEqualTo(savedItem.getDriveId());
        assertThat(actualEvent.getParentId()).isEqualTo(savedItem.getParentId());
        assertThat(actualEvent.getFileId()).isEqualTo(savedItem.getIdToString());

        /*== 리턴 객체 검증 ==*/
        assertThat(result).isNotNull();
        assertThat(result.driveItem()).isEqualTo(savedItem);


    }

    @Test
    @DisplayName("UploadCallback 테이터를 DriveItemRepository 저장에 성공하면, 저장된 아이템 데이터로 UserItemState 를 저장한다.")
    void saveUserItemState_success() {
        //given
        DriveItem savedItem = DriveItemFactory.createFrom(
                "driveId",
                rootFolder(),
                UUID.randomUUID().toString(),
                "text/plain",
                10L,
                "test.txt",
                "user1"
        );

        doReturn(Optional.of(rootFolder())).when(driveItemRepository).findById(any());
        doReturn(savedItem).when(driveItemRepository).save(any());
        ArgumentCaptor<UserItemState> stateCaptor = ArgumentCaptor.forClass(UserItemState.class);

        //when
        driveItemAppService.registerUploadedFile(uploadCallbackCommand());

        //then
        verify(userItemStateRepository).save(stateCaptor.capture());
        UserItemState actualValue = stateCaptor.getValue();

        assertThat(actualValue).isNotNull();
        assertThat(actualValue.getId()).isNotNull();
        assertThat(actualValue.getFileId()).isEqualTo(savedItem.getId());
        assertThat(actualValue.getCreatedBy()).isEqualTo(savedItem.getCreatedBy());

    }


    @Test
    @DisplayName("폴더 생성 요청 시, 지정한 parentId와 이름, 마임타입으로 폴더가 저장된다")
    void shouldCreateFolder_whenRequestCreateFolder() {
        //given
        FolderCreateCommand folderCreateCommand = folderCreateCommand();
        doReturn(folder()).when(driveItemRepository).save(any(DriveItem.class));
        doReturn(Optional.of(folder())).when(driveItemRepository).findById(any());

        //when
        DriveItem createdFolder = driveItemAppService.createFolder(folderCreateCommand);

        //then
        assertThat(createdFolder).isNotNull();
        assertThat(createdFolder.getDriveId()).isEqualTo(folderCreateCommand.driveId());
        assertThat(createdFolder.getParentId()).isEqualTo(folderCreateCommand.parentId());
    }

    public FolderCreateCommand folderCreateCommand() {
        String driveId = "d1234";
        String parentId = "root";
        String fileName = "folder1";
        return new FolderCreateCommand(driveId, parentId, MimeType.FOLDER, fileName);
    }

    private DriveItem folder() {
        return DriveItem.from(
                "d1234",
                "myFolder",
                "application/vnd.mini-drive.folder",
                0L,                   // 폴더이므로 size는 0
                rootFolder()
        );
    }

    private DriveItem rootFolder() {
        return DriveItem.builder()
                .id(UUID.randomUUID())
                .driveId("d1234")
                .metadata(FileMetadata.ofFolder(MimeType.from("application/vnd.mini-drive.folder"), "root"))
                .parent(null)
                .build();
    }

    private UploadCallbackCommand uploadCallbackCommand() {
        String mime = MimeType.FOLDER.getMime();
        String fileId = UUID.randomUUID().toString();
        String driveId = "d1234";
        String parentId = "root";
        String fileName = "text.txt";
        String userId = "user1";
        long size = 1234L;
        return new UploadCallbackCommand(mime, driveId, parentId, fileId, fileName, size, userId);
    }

}