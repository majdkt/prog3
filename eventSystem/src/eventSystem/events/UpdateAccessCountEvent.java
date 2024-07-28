package eventSystem.events;

import eventSystem.Event;

public class UpdateAccessCountEvent implements Event {
    private final String mediaAddress;

    public UpdateAccessCountEvent(String mediaAddress) {
        this.mediaAddress = mediaAddress;
    }

    public String getAddress() {
        return mediaAddress;
    }
}
