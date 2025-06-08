package dev.chan.domain.file;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(exclude = {"parent", "children"})
public class DriveItem {
    private static final String ROOT_ID = "root";

    private String id;
    private String name;
    private MimeType mimeType;
    private String driveId;
    private String fileKey;
    private Long size;

    private DriveItem parent;

    @Builder.Default
    private ArrayList<DriveItem> children = new ArrayList<>();

    public DriveItem(String driveId, String id, String name, MimeType mimeType, String fileKey, Long size, DriveItem parent) {
        this.driveId = driveId;
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.fileKey = fileKey;
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
     * 부모 경로 + 부모 이름으로 경로를 생성
     *
     * @param parentPath 부모 경로
     * @param parentName 부모 이름
     * @return
     */
    protected String createPath(String parentPath, String parentName) {
        if (parentPath == null || parentPath.isEmpty()) {
            return parentName;
        }
        return parentPath + "/" + parentName;
    }


    public DriveItem createChildFile(FileMetaData metadata, String part) {
        // TODO : 파일 생성 로직 구현 예정입니다.
        return null;
    }

    public Optional<DriveItem> findChildrenByName(String folderName) {
        // TODO : 자식 폴더 조회 로직 구현 예정입니다.
        return Optional.empty();
    }

    public DriveItem createChildFolder(String folderName) {
        // TODO : 자식 폴더 생성 로직 구현 예정입니다.
        return null;

    }

    public void addChild(DriveItem file) {
        // TODO : 자식 폴더 추가 로직 구현 예정입니다.
    }

    public void moveTo(DriveItem newParent) {
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
        this.fileKey = originalItem.getFileKey();
        this.size = originalItem.getSize();
        this.parent = originalItem.getParent();
        this.children = originalItem.getChildren() != null ? new ArrayList<>(originalItem.getChildren()) : new ArrayList<>();
    }
}
