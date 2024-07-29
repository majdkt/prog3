package eventSystem.events;

import eventSystem.Event;

public class DeleteUploaderEvent implements Event {
    private final String uploaderName;

    public DeleteUploaderEvent(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderName() {
        return uploaderName;
    }
}
