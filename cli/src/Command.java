public class Command {
    private final String action;
    private final String address;
    private final long accessCount;

    public Command(String action) {
        this(action, null, 0);
    }

    public Command(String action, String address) {
        this(action, address, 0);
    }

    public Command(String action, String address, long accessCount) {
        this.action = action;
        this.address = address;
        this.accessCount = accessCount;
    }

    public String getAction() {
        return action;
    }

    public String getAddress() {
        return address;
    }

    public long getAccessCount() {
        return accessCount;
    }
}
