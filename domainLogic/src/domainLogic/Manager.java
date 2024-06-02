package domainLogic;

import contract.Audio;

import java.util.*;
import java.util.stream.Collectors;

public class Manager {
    private Map<String, Audio> audioMap = new HashMap<>();
    private int addressCounter = 1;
    private Queue<String> availableAddresses = new LinkedList<>();

    public void create() {
        String address;
        if (availableAddresses.isEmpty()) {
            address = "address_" + addressCounter;
            addressCounter++;
        } else {
            address = availableAddresses.poll();
        }
        Audio audioFile = new AudioImpl(address);
        audioMap.put(address, audioFile);
    }

    public List<String> read() {
        List<String> audioDetails = new ArrayList<>();
        for (Map.Entry<String, Audio> entry : audioMap.entrySet()) {
            audioDetails.add(entry.getValue().toString());
        }
        return audioDetails;
    }

    public List<Audio> getAudioList() {
        return new ArrayList<>(audioMap.values());
    }

    public void update(String address, long newAccessCount) {
        Audio audio = audioMap.get(address);
        if (audio != null) {
            System.out.println("old " + audio.getAccessCount());
            ((AudioImpl) audio).setAccessCount(newAccessCount);
            System.out.println("new " + audio.getAccessCount());
        }
    }

    public void delete(String address) {
        Audio removedAudio = audioMap.remove(address);
        if (removedAudio != null) {
            availableAddresses.add(address);
        }
    }
}
