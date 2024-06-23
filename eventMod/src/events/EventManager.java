package events;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<AudioEventListener> listeners = new ArrayList<>();

    public synchronized void addListener(AudioEventListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(AudioEventListener listener) {
        listeners.remove(listener);
    }

    public synchronized void notifyListeners(AudioEvent event) {
        for (AudioEventListener listener : listeners) {
            listener.handleAudioEvent(event);
        }
    }

    public synchronized List<AudioEventListener> getListeners() {
        return new ArrayList<>(listeners);
    }

    public synchronized void setListeners(List<AudioEventListener> listeners) {
        this.listeners = listeners;
    }
}
