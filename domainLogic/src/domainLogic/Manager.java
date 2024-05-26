package domainLogic;

import contract.MediaContent;
import contract.Uploader;

import java.util.*;

public class Manager {
    private final List<MediaContent> mediaList;
    private final Map<MediaContent, Uploader> taskAssignments;

    public Manager() {
        this.mediaList = new ArrayList<>();
        this.taskAssignments = new HashMap<>();
    }

    public void create(MediaContent mediaContent, Uploader uploader) {
        mediaList.add(mediaContent);
        taskAssignments.put(mediaContent, uploader);
    }

    public List<MediaContent> read() {
        return new ArrayList<>(this.mediaList);
    }

    public void update(int index) {
        if (index < 0 || index >= mediaList.size()) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        MediaContent mediaContent = mediaList.get(index);
        mediaContent.setAccessCount(mediaContent.getAccessCount() + 1);
    }

    public boolean delete(MediaContent content) {
        taskAssignments.remove(content);
        return mediaList.remove(content);
    }

    public Uploader getUploader(MediaContent content) {
        return taskAssignments.get(content);
    }
}


