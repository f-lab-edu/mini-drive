package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
public class DriveItem {
    /*== 필수 필드 ==*/
    private UUID id;
    private String driveId;
    private FileMetadata metadata;
    private LocalDateTime createdAt;
    private Thumbnail thumbnail;
    private String createdBy;

    /*== 아이템 상태 필드 ==*/
    private boolean locked;
    private boolean deleted;

    /*== 변경 가능한 필드 ==*/
    private DriveItem parent;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<DriveItem> children = new ArrayList<>();

    private DriveItem(String driveId, String fileName, Long size, String mimeType, String createdBy, DriveItem parent, ArrayList<DriveItem> children) {
        this.id = UUID.randomUUID();
        this.driveId = driveId;
        this.metadata = new FileMetadata(mimeType, fileName, size);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = createdBy;
        this.parent = parent;
        this.children = children;
    }

    public static DriveItem from(String driveId, String fileName, String mimeType, long size, DriveItem parent) {
        return DriveItem.builder()
                .driveId(driveId)
                .id(UUID.randomUUID())
                .metadata(new FileMetadata(mimeType, fileName, size))
                .parent(parent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static DriveItem of(UUID id,
                               String driveId,
                               String fileName,
                               Long fileSize,
                               String mimeType,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               String createdBy,
                               DriveItem parent,
                               List<DriveItem> children) {

        return DriveItem.builder()
                .id(UUID.randomUUID())
                .driveId(driveId)
                .metadata(new FileMetadata(mimeType, fileName, fileSize))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .parent(parent)
                .children(children)
                .build();
    }

    public static DriveItem ofRoot(String driveId) {
        log.info("[ofRoot] {}", driveId);
        return DriveItem.builder()
                .id(UUID.randomUUID())
                .metadata(FileMetadata.ofRoot())
                .driveId(driveId)
                .parent(null)
                .build();
    }

    public String getIdToString() {
        return id.toString();
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

    public String getParentId() {
        return this.getParent().getIdToString();
    }

    // fileKey 필드를 따로 두지않고 편의성 메서드로 관리하시 위한 메서드
    public String fileKey(String prefix) {
        return prefix + this.driveId + "/files/" + this.getId();
    }

    public Optional<Thumbnail> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    // URL 생성을 위한 키 생성 편의성 메서드
    public String thumbnailKey(String prefix) {
        return "/" + prefix + "/" + id;
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
    public void moveTo(DriveItem newParent) {
        if (!newParent.isFolder()) throw new IllegalArgumentException("Cannot move to file");
        this.parent = newParent;
    }

}
