package dev.chan.application.file;


import dev.chan.application.command.FolderCreateCommand;
import dev.chan.application.command.UploadCallbackCommand;
import dev.chan.application.vo.UploadCallbackResult;
import dev.chan.common.MimeType;
import dev.chan.domain.UploadedFileRegisteredEvent;
import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemFactory;
import dev.chan.domain.file.FileMetadata;
import dev.chan.domain.publisher.DriveItemEventPublisher;
import dev.chan.infrastructure.MemoryDriveItemRepository;
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
    MemoryDriveItemRepository driveItemRepository;

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
        doReturn(Optional.of(parent)).when(driveItemRepository).findById(any());
        doReturn(savedItem).when(driveItemRepository).save(any());

        ArgumentCaptor<DriveItem> itemCaptor = ArgumentCaptor.forClass(DriveItem.class);
        ArgumentCaptor<UploadedFileRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UploadedFileRegisteredEvent.class);

        // when
        UploadCallbackResult result = driveItemAppService.registerUploadedFile(command);

        // then
        verify(driveItemRepository, times(1)).save(itemCaptor.capture());
        verify(domainEventPublisher, times(1)).publish(eventCaptor.capture());

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