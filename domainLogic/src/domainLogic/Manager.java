package domainLogic;

import contract.Audio;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private List<AudioImpl> audioList = new ArrayList<>();

    Audio create(int SamplingRate, String adress){
        AudioImpl ai = new AudioImpl(SamplingRate,adress); // Pass the SamplingRate to the AudioImpl constructor
        audioList.add(ai);
        return null;
    }

    public List<Audio> read(){
        return new ArrayList<>(this.audioList);
    }

    Audio update(int i){
        AudioImpl audio = audioList.get(i);
        long ac = audio.getAccessCount();
        audio.setAccessCount(ac+1);
        return audio;
    }

    boolean delete(Audio audio){
        return audioList.remove(audio);
    }

}
