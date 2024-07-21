package events;

public class AccessCountUpdatedEvent extends Event {
    private final String address;

    public AccessCountUpdatedEvent(String address) {
        this.address = address;
    }

    public String getAddress() { return address; }
}
