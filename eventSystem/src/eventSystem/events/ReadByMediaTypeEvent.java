package eventSystem.events;

import eventSystem.Event;

public class ReadByMediaTypeEvent implements Event {
    private final String mediaType;

    public ReadByMediaTypeEvent(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}
