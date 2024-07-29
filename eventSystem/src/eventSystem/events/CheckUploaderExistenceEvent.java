package eventSystem.events;

import eventSystem.Event;

public class CheckUploaderExistenceEvent implements Event {
    private final String uploaderName;
    private boolean exists;

    public CheckUploaderExistenceEvent(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
