package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.UpdateAccessCountEvent;

public class UpdateAccessCountEventListener implements EventListener {
    private Manager manager;

    public UpdateAccessCountEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof UpdateAccessCountEvent) {
            UpdateAccessCountEvent updateAccessCountEvent = (UpdateAccessCountEvent) event;
            manager.updateAccessCount(updateAccessCountEvent.getAddress());
        }
    }
}
