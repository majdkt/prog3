package events;

public class UploaderDeletedEvent extends Event {
    private final String uploaderName;

    public UploaderDeletedEvent(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderName() { return uploaderName; }
}
