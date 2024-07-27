package eventSystem;

public class MediaEvent extends Event {
    private final String address;

    public MediaEvent(Object source, String address) {
        super(source);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
