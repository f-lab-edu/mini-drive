package dev.chan.domain.file;

import dev.chan.common.MimeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"parent", "children"})
public class DriveItem {
    // 루트 폴더 ID - 루트 디렉토리도 폴더로 취급.
    private static final String ROOT_ID = "root";

    private String id;
    private String driveId;
    private String name;
    private Long size;
    private MimeType mimeType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private DriveItem parent;

    @Builder.Default
    private ArrayList<DriveItem> children = new ArrayList<>();

    // fileKey 필드를 따로 두지않고 편의성 메서드로 관리하시 위한 메서드
    public String fileKey() {
        return this.driveId + "/files/" + this.id;
    }

    // URL 생성을 위한 키 생성 편의성 메서드
    public String thumbnailKey() {
        return "thumbnails/" + id;
    }

    // 썸네일 생성전 클라이언트 응답을 위한 편의성 메서드
    public String thumbnailUrl(String domain) {
        return domain + "/" + thumbnailKey();
    }

    public DriveItem(String driveId, String id, String name, MimeType mimeType, Long size, DriveItem parent) {
        this.driveId = driveId;
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
        this.parent = parent;
    }

    public String getParentId() {
        if (this.getParent() == null) {
            return ROOT_ID;
        }
        return this.getParent().getId();
    }

    /**
     * DriveItem 타입이 폴더인지 확인
     *
     * @return
     */
    public boolean isFolder() {
        return this.mimeType == MimeType.FOLDER;
    }

    /**
     * 현재 아이템을 부모 폴더 하위로 이동합니다.
     *
     * @param newParent
     */
    public void moveTo(DriveItem newParent) {
        if (!newParent.isFolder()) {
            throw new IllegalArgumentException("Cannot move to file");
        }

        this.parent = newParent;
    }

    /**
     * 현재 DriveItem의 속성을 원본 DriveItem에서 복사합니다.
     *
     * @param originalItem 복사할 원본 DriveItem
     */
    public void copyOf(DriveItem originalItem) {
        this.driveId = originalItem.getDriveId();
        this.id = originalItem.getId();
        this.name = originalItem.getName();
        this.mimeType = originalItem.getMimeType();
        this.size = originalItem.getSize();
        this.parent = originalItem.getParent();
        this.children = originalItem.getChildren() != null ? new ArrayList<>(originalItem.getChildren()) : new ArrayList<>();
    }
}
