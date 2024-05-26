package domainLogic;

import contract.MediaContent;
import contract.Uploader;

import java.util.*;

public class Manager {
    private final List<MediaContent> mediaList = new ArrayList<>();
    private final Map<MediaContent, Uploader> taskAssignments = new HashMap<>();

     public void create(MediaContent mediaContent, Uploader uploader) {
        mediaList.add(mediaContent);
        taskAssignments.put(mediaContent,uploader);
    }

    public List<MediaContent> read() {
        return new ArrayList<>(this.mediaList);
    }

    public void update(int i) {mediaList.get(i).setAccessCount();}

    public boolean delete(MediaContent content) {
        return mediaList.remove(content);
    }

 }

