package domainLogic;

import contract.Audio;
import java.util.*;

public class Manager {
    private final Map<String, Audio> audioMap = new HashMap<>();
    private int addressCounter = 1;
    private final Queue<String> availableAddresses = new LinkedList<>();

    public synchronized void create() {
        String address;
        if (availableAddresses.isEmpty()) {
            address = "address_" + addressCounter;
            addressCounter++;
            System.out.println("Creating " + address);
        } else {
            address = availableAddresses.poll();
        }
        Audio audioFile = new AudioImpl(address);
        audioMap.put(address, audioFile);
    }

    public synchronized List<String> read() {
        List<String> audioDetails = new ArrayList<>();
        for (Map.Entry<String, Audio> entry : audioMap.entrySet()) {
            audioDetails.add(entry.getValue().toString());
        }
        return audioDetails;
    }

    public synchronized void update(String address, long newAccessCount) {
        Audio audio = audioMap.get(address);
        if (audio != null) {
            System.out.println("Old access count: " + audio.getAccessCount());
            ((AudioImpl) audio).setAccessCount(newAccessCount);
            System.out.println("New access count: " + audio.getAccessCount());
        }
    }

    public synchronized void delete(String address) {
        if (audioMap.remove(address)!=null){
            Audio removedAudio = audioMap.remove(address);
            if (removedAudio != null) {
                availableAddresses.add(address);
            }
            System.out.println( "Removed address: " + address );
        }
    }
}
