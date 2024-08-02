package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.DeleteEvent;
import eventSystem.events.StateUpdatedEvent;

public class DeleteEventListener implements EventListener {
    private Manager manager;

    public DeleteEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof DeleteEvent) {
            DeleteEvent deleteEvent = (DeleteEvent) event;
            manager.deleteMedia(deleteEvent.getTarget());
        } else if (event instanceof StateUpdatedEvent) {
            this.manager = ((StateUpdatedEvent) event).getNewManager();
        }
    }
}
