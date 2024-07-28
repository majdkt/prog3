package eventSystem.events;

import eventSystem.Event;

public class DeleteEvent implements Event {
    private final String target;

    public DeleteEvent(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
