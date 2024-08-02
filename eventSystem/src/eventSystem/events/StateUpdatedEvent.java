package eventSystem.events;

import domainLogic.Manager;
import eventSystem.Event;

public class StateUpdatedEvent implements Event {
    private Manager newManager;

    public StateUpdatedEvent(Manager newManager) {
        this.newManager = newManager;
    }

    public Manager getNewManager() {
        return newManager;
    }
}
