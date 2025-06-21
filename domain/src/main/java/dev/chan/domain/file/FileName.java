package dev.chan.domain.file;

import lombok.Getter;

@Getter
public class FileName {

    public final static String ROOT_NAME = "root";
    private final String fullName;

    public FileName(String fullName) {
        if (fullName == null) {
            throw new IllegalArgumentException("파일 이름은 null이거나 비어 있을 수 없습니다.");
        }
        this.fullName = fullName;
    }

    public String getExtension() {
        int dotIndex = fullName.lastIndexOf(".");
        if (dotIndex == -1) return "";
        return fullName.substring(dotIndex + 1).toLowerCase();
    }

    public String getBaseName() {
        return null;
    }

    public static FileName ofRoot() {
        return new FileName(ROOT_NAME);
    }
}
