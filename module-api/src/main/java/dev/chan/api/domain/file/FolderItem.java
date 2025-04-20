package dev.chan.api.domain.file;

import java.util.ArrayList;
import java.util.List;

public class FolderItem extends DriveItem{
    private List<FileItem> children = new ArrayList<>();
}
