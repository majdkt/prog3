package eventSystem;

import eventSystem.listeners.EventListener;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {
    private final List<EventListener> listeners = new ArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void dispatch(Event event) {
        for (EventListener listener : listeners) {
            listener.handleEvent(event);
        }
    }
}
