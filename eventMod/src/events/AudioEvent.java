package events;

import java.util.EventObject;


public class AudioEvent extends EventObject {
    public enum EventType {
        CREATE, READ, UPDATE, DELETE, LOGOUT
    }

    private EventType eventType;
    private String address;
    private long newAccessCount;

    public AudioEvent(Object source, EventType eventType) {
        super(source);
        this.eventType = eventType;
    }

    public AudioEvent(Object source, EventType eventType, String address, long newAccessCount) {
        super(source);
        this.eventType = eventType;
        this.address = address;
        this.newAccessCount = newAccessCount;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getAddress() {
        return address;
    }

    public long getNewAccessCount() {
        return newAccessCount;
    }
}
