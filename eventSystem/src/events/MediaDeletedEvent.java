package events;

public class MediaDeletedEvent extends Event {
    private final String address;

    public MediaDeletedEvent(String address) {
        this.address = address;
    }

    public String getAddress() { return address; }
}
