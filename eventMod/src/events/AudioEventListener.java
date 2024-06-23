package events;


import java.util.EventListener;

public interface AudioEventListener extends EventListener {
    void handleAudioEvent(AudioEvent event);
}
