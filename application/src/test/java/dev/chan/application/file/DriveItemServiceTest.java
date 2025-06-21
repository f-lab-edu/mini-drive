package dev.chan.application.file;


import dev.chan.application.command.FolderCreateCommand;
import dev.chan.application.command.UploadCallbackCommand;
import dev.chan.common.MimeType;
import dev.chan.domain.file.DriveItem;
import dev.chan.domain.file.DriveItemRepository;
import dev.chan.domain.file.FileMetadata;
import dev.chan.domain.publisher.DriveItemEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriveItemServiceTest {

    @Mock
    DriveItemEventPublisher domainEventPublisher;

    @Mock
    DriveItemRepository driveItemRepository;

    @InjectMocks
    DriveItemService driveItemService;

    @Test
    @DisplayName("업로드 콜백 처리 명령를 받으면 DB에 메타데이터를 저장한다. ")
    void shouldSaveMetadata_WhenUploadCallbackReceived() {
        // given
        DriveItem parent = rootFolder();
        UploadCallbackCommand command = uploadCallbackCommand();
        doReturn(Optional.of(parent)).when(driveItemRepository).findById(any());

        doAnswer(invocation -> invocation.getArgument(0)).when(driveItemRepository).save(any());

        // when
        DriveItem registered = driveItemService.registerUploadedFile(command);

        // then
        verify(domainEventPublisher, times(1)).publish(any());
        verify(driveItemRepository, times(1)).save(any());

        assertThat(registered).isNotNull();
        assertThat(registered.getFileName()).isEqualTo(command.fileName());
        assertThat(registered.getMimeType()).isEqualTo(command.mimeType());
        assertThat(registered.getSize()).isEqualTo(command.size());
        assertThat(registered.getParent()).isEqualTo(parent);
    }

    @Test
    @DisplayName("폴더 생성 요청 시, 지정한 parentId와 이름, 마임타입으로 폴더가 저장된다")
    void shouldCreateFolder_whenRequestCreateFolder() {
        //given
        FolderCreateCommand folderCreateCommand = folderCreateCommand();
        doReturn(folder()).when(driveItemRepository).save(any(DriveItem.class));
        doReturn(Optional.of(folder())).when(driveItemRepository).findById(any());

        //when
        DriveItem createdFolder = driveItemService.createFolder(folderCreateCommand);

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
                MimeType.from("application/vnd.mini-drive.folder"),
                0L,                   // 폴더이므로 size는 0
                rootFolder()
        );
    }

    private DriveItem rootFolder() {
        return DriveItem.builder()
                .driveId("d1234")
                .metadata(FileMetadata.ofFolder(MimeType.from("application/vnd.mini-drive.folder"), "fileName"))
                .parent(null)
                .build();
    }

    private UploadCallbackCommand uploadCallbackCommand() {
        MimeType mime = MimeType.FOLDER;
        String fileKey = "fileKey";
        String driveId = "d1234";
        String parentId = "root";
        String fileName = "text.txt";
        long size = 1234L;
        return new UploadCallbackCommand(mime, driveId, parentId, fileKey, fileName, size);
    }

}