package domainLogic;

import contract.Audio;
import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Manager {
    private List<MediaContent> mediaList = new ArrayList<>();
    private Map<MediaContent, Uploadable> taskAssignments = new HashMap<>();



    Audio create(MediaContent content, Uploadable uploadable){
        mediaList.add(content);
        taskAssignments.put(content, uploadable);
        return null;
    }

    public List<MediaContent> read(){
        return new ArrayList<>(this.mediaList);
    }

    MediaContent update(int i){
        MediaContent content = mediaList.get(i);
        long ac = content.getAccessCount();
        return content;
    }

    boolean delete(MediaContent content){
        return mediaList.remove(content);
    }
    
}
