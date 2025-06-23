package dev.chan.fixture;

import dev.chan.common.MimeType;
import dev.chan.domain.file.DriveItemId;
import dev.chan.domain.file.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@ToString(exclude = {"parent", "children"})
public class DriveItemFixture {
    /*== 필수 필드 ==*/
    private final DriveItemId id;
    private final String driveId;
    private final FileMetadata metadata;
    private final LocalDateTime createdAt;
    private final String createdBy;

    /*== 아이템 상태 필드 ==*/
    private boolean locked;
    private boolean deleted;

    /*== 변경 가능한 필드 ==*/
    private DriveItemFixture parent;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<DriveItemFixture> children = new ArrayList<>();

    private DriveItemFixture(String driveId, String fileName, Long size, String mimeType, String createdBy, DriveItemFixture parent, ArrayList<DriveItemFixture> children) {
        this.id = new DriveItemId();
        this.driveId = driveId;
        this.metadata = new FileMetadata(mimeType, fileName, size);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = createdBy;
        this.parent = parent;
        this.children = children;
    }

    public static DriveItemFixture from(String driveId, String fileName, String mimeType, long size, DriveItemFixture parent) {
        return DriveItemFixture.builder()
                .driveId(driveId)
                .id(new DriveItemId())
                .metadata(new FileMetadata(mimeType, fileName, size))
                .parent(parent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static DriveItemFixture of(UUID id,
                                      String driveId,
                                      String fileName,
                                      Long fileSize,
                                      String mimeType,
                                      LocalDateTime createdAt,
                                      LocalDateTime updatedAt,
                                      String createdBy,
                                      DriveItemFixture parent,
                                      List<DriveItemFixture> children) {

        return DriveItemFixture.builder()
                .id(new DriveItemId(id))
                .driveId(driveId)
                .metadata(new FileMetadata(mimeType, fileName, fileSize))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .parent(parent)
                .children(children)
                .build();
    }

    public static DriveItemFixture ofRoot(String driveId) {
        return DriveItemFixture.builder()
                .id(new DriveItemId())
                .metadata(FileMetadata.ofRoot())
                .driveId(driveId)
                .parent(null)
                .build();
    }

    public UUID getId() {
        return id.getValue();
    }

    public String getIdToString() {
        return id.getValue().toString();
    }

    public long getSize() {
        return metadata.getFileSize().getBytes();
    }

    public String getFileName() {
        return metadata.getFileName().getFullName();
    }

    public MimeType getMimeType() {
        return metadata.getMimeType();
    }

    // fileKey 필드를 따로 두지않고 편의성 메서드로 관리하시 위한 메서드
    public String fileKey() {
        return this.driveId + "/files/" + this.id;
    }

    public String getParentId() {
        return this.getParent().getIdToString();
    }

    // URL 생성을 위한 키 생성 편의성 메서드
    public String thumbnailKey() {
        return "thumbnails/" + id;
    }

    /**
     * 객체가 폴더 or 파일인지 확인하여 boolean 값을 리턴
     * FileMetadata.MimeType 을 확인해 폴더/파일 여부를 확인합니다.
     *
     * @return
     */
    public boolean isFolder() {
        return metadata.getMimeType() == MimeType.FOLDER;
    }

    public boolean isCreatedBy(String userId) {
        return Objects.equals(userId, createdBy);
    }

    /**
     * 현재 객체를 인자로 받는 DriveItem 객체의 하위 노드로 등록
     *
     * @param newParent
     */
    public void moveTo(DriveItemFixture newParent) {
        if (!newParent.isFolder()) throw new IllegalArgumentException("Cannot move to file");
        this.parent = newParent;
    }

}
