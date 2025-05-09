package dev.chan.api.domain.file;

import dev.chan.api.application.file.command.UploadCallbackCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
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

    public DriveItem(String driveId, String id, String name, MimeType mimeType, String fileKey, Long size) {


    }

    public String getParentId() {
        if (this.getParent() == null) {
            return ROOT_ID;
        }
        return this.getParent().getId();
    }

    /**
     * DriveItem 타입이 폴더인지 확인
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


    public DriveItem createChildFile(FileMetaData metadata, String part){
        // TODO : 파일 생성 로직 구현 예정입니다.
        return null;
    };

    public Optional<DriveItem> findChildrenByName(String folderName){
        // TODO : 자식 폴더 조회 로직 구현 예정입니다.
        return Optional.empty();
    };

    public DriveItem createChildFolder(String folderName){
        // TODO : 자식 폴더 생성 로직 구현 예정입니다.
        return null;

    };

    public void addChild(DriveItem file) {
        // TODO : 자식 폴더 추가 로직 구현 예정입니다.
    }


    public void moveTo(DriveItem newParent) {
        this.parent = newParent;
    }
}
