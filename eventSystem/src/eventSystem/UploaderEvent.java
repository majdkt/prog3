package eventSystem;

public class UploaderEvent extends Event {
    private final String uploaderName;

    public UploaderEvent(Object source, String uploaderName) {
        super(source);
        this.uploaderName = uploaderName;
    }

    public String getUploaderName() {
        return uploaderName;
    }
}
