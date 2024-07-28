package eventSystem.events;

import eventSystem.Event;

public class UpdateAccessCountEvent extends Event {
    private final String mediaAddress;

    public UpdateAccessCountEvent(String mediaAddress) {
        this.mediaAddress = mediaAddress;
    }

    public String getMediaAddress() {
        return mediaAddress;
    }
}
