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
    private String mimeType;
    private String driveId;
    private String fileKey;
    private Long size;

    private DriveItem parent;

    @Builder.Default
    private ArrayList<DriveItem> children = new ArrayList<>();

    public DriveItem(String id, String name, String mimeType) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
    }

    public DriveItem(String d1234, Object o, String root, String root1, String s, String s1) {

    }

    public String getParentId() {
        if (this.getParent() == null) {
            return ROOT_ID;
        }

        return this.getParent().getId();
    }


    public boolean isFolder() {
        // TODO: 구현 준비중입니다.
        return true;

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
