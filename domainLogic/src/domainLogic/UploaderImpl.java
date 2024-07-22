package domainLogic;

import contract.Uploader;

import java.util.ArrayList;
import java.util.List;

public class UploaderImpl implements Uploader {
    private String name;
    private List<String> mediaList;

    public UploaderImpl(String name) {
        this.name = name;
        this.mediaList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addMedia(String address) {
        mediaList.add(address);
    }

    public List<String> getMediaList() {
        return mediaList;
    }
}
