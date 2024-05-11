package domainLogic;

import contract.MediaContent;
import contract.Uploadable;
import java.util.*;

public class Manager {
    private final List<MediaContent> mediaList = new ArrayList<>();
    private final Map<MediaContent, Uploadable> taskAssignments = new HashMap<>();

    public String getCurrentUser() {
        return currentUser;
    }

    private String currentUser;

    public Manager(String currentUser) {
        this.currentUser = currentUser;
    }


     public void create(MediaContent mediaContent, Uploadable uploadable) {
        mediaList.add(mediaContent);
        taskAssignments.put(mediaContent,uploadable);
    }

    public List<MediaContent> read() {
        return new ArrayList<>(this.mediaList);
    }

    private void update(MediaContent content,int i) {
        MediaContent mc = null;
        mediaList.add(content);
        mediaList.remove(i);
    }

    public boolean delete(MediaContent content) {
        return mediaList.remove(content);
    }

 }

