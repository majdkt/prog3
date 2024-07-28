package eventSystem.events;

import eventSystem.Event;

public class CreateUploaderEvent implements Event {
    private final String uploaderName;

    public CreateUploaderEvent(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderName() {
        return uploaderName;
    }
}
