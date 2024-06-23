package domainLogic;

import contract.Audio;
import events.AudioEvent;
import events.EventManager;

import java.io.*;
import java.util.*;

public class Manager implements Serializable {
    private Map<String, AudioImpl> audioMap = new HashMap<>();
    private int addressCounter = 1;
    private Queue<String> availableAddresses = new LinkedList<>();
    private transient EventManager eventManager = new EventManager();

    public Manager() {
        this.eventManager = new EventManager();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.eventManager = new EventManager();
    }

    public synchronized void create() {
        String address;
        if (availableAddresses.isEmpty()) {
            address = "address_" + addressCounter;
            addressCounter++;
        } else {
            address = availableAddresses.poll();
        }
        AudioImpl audioFile = new AudioImpl(address);
        audioMap.put(address, audioFile);
        eventManager.notifyListeners(new AudioEvent(this, AudioEvent.EventType.CREATE));
    }

    public synchronized List<String> read() {
        List<String> audioDetails = new ArrayList<>();
        for (Map.Entry<String, AudioImpl> entry : audioMap.entrySet()) {
            audioDetails.add(entry.getValue().toString());
        }
        eventManager.notifyListeners(new AudioEvent(this, AudioEvent.EventType.READ));
        return audioDetails;
    }

    public synchronized void update(String address, long newAccessCount) {
        AudioImpl audio = audioMap.get(address);
        if (audio != null) {
            audio.setAccessCount(newAccessCount);
            eventManager.notifyListeners(new AudioEvent(this, AudioEvent.EventType.UPDATE, address, newAccessCount));
        }
    }

    public synchronized void delete(String address) {
        if (audioMap.remove(address) != null) {
            availableAddresses.add(address);
            eventManager.notifyListeners(new AudioEvent(this, AudioEvent.EventType.DELETE, address, 0));
        }
    }

    public synchronized List<Audio> getAudioList() {
        return new ArrayList<>(audioMap.values());
    }

    public synchronized void logout() {
        audioMap.clear();
        addressCounter = 0;
        availableAddresses.clear();
        eventManager.notifyListeners(new AudioEvent(this, AudioEvent.EventType.LOGOUT));
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
