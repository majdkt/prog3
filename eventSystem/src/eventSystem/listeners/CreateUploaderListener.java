package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.CreateUploaderEvent;

public class CreateUploaderListener implements EventListener {
    private Manager manager;

    public CreateUploaderListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof CreateUploaderEvent) {
            CreateUploaderEvent createUploaderEvent = (CreateUploaderEvent) event;
            String uploaderName = createUploaderEvent.getUploaderName();
            manager.createUploader(uploaderName);
        }
    }
}
