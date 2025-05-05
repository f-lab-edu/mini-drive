package dev.chan.api.application.file;


import dev.chan.api.application.file.command.UploadCallbackCommand;
import dev.chan.api.domain.file.DriveItem;
import dev.chan.api.domain.file.DriveItemRepository;
import dev.chan.api.domain.file.exception.DriveItemNotFoundException;
import dev.chan.api.infrastructure.storage.LocalFileStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriveItemServiceTest {

    @Mock
    LocalFileStorage fileStorage;

    @Mock
    DriveItemRepository driveItemRepository ;

    @InjectMocks
    DriveItemService driveItemService;

    @Test
    @DisplayName("test.txt 파일이 존재할 때, 업로드 콜백을 받으면 메타데이터가 저장된다")
    void shouldSaveMetadata_WhenUploadCallbackReceived(){
        // given
        DriveItem parent = rootFolder();
        UploadCallbackCommand command = uploadCallbackCommand();
        doReturn(Optional.of(parent)).when(driveItemRepository).findById(any());
        doAnswer(invocation -> invocation.getArgument(0)).when(driveItemRepository).save(any());

        // when
        DriveItem createdFile = driveItemService.create(command);

        // then
        assertThat(createdFile).isNotNull();
        assertThat(createdFile.getName()).isEqualTo(command.getFileName());
        assertThat(createdFile.getMimeType()).isEqualTo(command.getMimeType());
        assertThat(createdFile.getSize()).isEqualTo(command.getSize());
        assertThat(createdFile.getFileKey()).isEqualTo(command.getFileKey());
        assertThat(createdFile.getParent()).isEqualTo(parent);
    }

    @Test
    @DisplayName("parent 객체를 조회시, parent객체가 null이면, DriveItemNotFoundException 예외를 던진다.")
    void shouldThrowDriveItemNotFoundException_WhenParentIsNull(){
        // given
        DriveItem parent = rootFolder();
        UploadCallbackCommand command = uploadCallbackCommand();
        doReturn(Optional.empty()).when(driveItemRepository).findById(any());

        // when & then
        assertThatThrownBy( ()-> driveItemService.create(command))
                .isInstanceOf(DriveItemNotFoundException.class)
                .hasMessageContaining(parent.getId());
    }

    private DriveItem file() {
        return DriveItem.builder()
                .driveId("d1234")
                .mimeType("plain/text")
                .name("test.txt")
                .fileKey("fileKey")
                .size(1234L)
                .build();
    }

    private DriveItem folder() {
        return DriveItem.builder()
                .driveId("d1234")
                .id("f12345")
                .name("myFolder")
                .mimeType("application/vnd.mini-drive.folder")
                .parent(rootFolder())
                .build();
    }

    private DriveItem rootFolder(){
        return DriveItem.builder()
                .driveId("d1234")
                .id("root")
                .mimeType("application/vnd.mini-drive.folder")
                .parent(null)
                .build();
    }

    private UploadCallbackCommand uploadCallbackCommand() {
        return UploadCallbackCommand.builder()
                .mimeType("plain/text")
                .fileKey("fileKey")
                .driveId("d1234")
                .parentId("root")
                .fileName("test.txt")
                .size(1234L)
                .build();
    }

}