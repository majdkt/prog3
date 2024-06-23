package domainLogic;

import contract.Audio;

import java.io.*;
import java.util.*;

public class Manager implements Serializable {
    private Map<String, AudioImpl> audioMap = new HashMap<>();
    private int addressCounter = 1;
    private Queue<String> availableAddresses = new LinkedList<>();

    public synchronized void create() {
        String address;
        if (availableAddresses.isEmpty()) {
            address = "address_" + addressCounter;
            addressCounter++;
            System.out.println("Saving audioFile in " + address);
        } else {
            address = availableAddresses.poll();
        }
        AudioImpl audioFile = new AudioImpl(address);
        audioMap.put(address, audioFile);
    }

    public synchronized List<String> read() {
        List<String> audioDetails = new ArrayList<>();
        for (Map.Entry<String, AudioImpl> entry : audioMap.entrySet()) {
            audioDetails.add(entry.getValue().toString());
        }
        return audioDetails;
    }

    public synchronized void update(String address, long newAccessCount) {
        AudioImpl audio = audioMap.get(address);
        if (audio != null) {
            System.out.println("Old access count: " + audio.getAccessCount());
            audio.setAccessCount(newAccessCount);
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

    public synchronized List<Audio> getAudioList() {
        return new ArrayList<>(audioMap.values());
    }

    public synchronized void logout() {
        audioMap.clear();
        addressCounter = 0;
        availableAddresses.clear();
        System.out.println("Logged out successfully. State has been reset.");
    }
}
