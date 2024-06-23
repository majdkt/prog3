package events;

public class AudioEventHandler implements AudioEventListener {
    @Override
    public void handleAudioEvent(AudioEvent event) {
        switch (event.getEventType()) {
            case CREATE:
                System.out.println("Handled CREATE event");
                break;
            case READ:
                System.out.println("Handled READ event");
                break;
            case UPDATE:
                System.out.println("Handled UPDATE event for address: " + event.getAddress() +
                        " with new access count: " + event.getNewAccessCount());
                break;
            case DELETE:
                System.out.println("Handled DELETE event for address: " + event.getAddress());
                break;
            case LOGOUT:
                System.out.println("Handled LOGOUT event");
                break;
        }
    }
}
