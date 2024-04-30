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
    private List<Audio> audioList = new ArrayList<>();
    private Map<Audio, Uploadable> taskAssignments = new HashMap<>();

    MediaContent create1(Uploadable uploadable) {



        return null;
    }



    Audio create(int SamplingRate, String adress, long size,Uploadable uploadable){
        Audio audioFile = new AudioImpl(SamplingRate, adress, size); // Pass the values to the AudioImpl constructor
        audioList.add(audioFile);
        taskAssignments.put(audioFile, uploadable);
        return null;
    }

    public List<Audio> read(){
        return new ArrayList<>(this.audioList);
    }

    Audio update(int i){
        Audio audioFile = audioList.get(i);
        long ac = audioFile.getAccessCount();
        return audioFile;
    }

    boolean delete(Audio audioFile){
        return audioList.remove(audioFile);
    }
    
}
