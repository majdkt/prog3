package eventSystem.listeners;

import eventSystem.Event;
import eventSystem.events.CreateUploaderEvent;
import eventSystem.events.DeleteEvent;
import domainLogic.Manager;

public class UploaderListener implements EventListener {
    private final Manager manager;

    public UploaderListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof CreateUploaderEvent) {
            CreateUploaderEvent createUploaderEvent = (CreateUploaderEvent) event;
            manager.createUploader(createUploaderEvent.getUploaderName());
        } else if (event instanceof DeleteEvent) {
            DeleteEvent deleteEvent = (DeleteEvent) event;
            manager.deleteMedia(deleteEvent.getTarget());
        }
    }
}
