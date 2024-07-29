package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.CreateUploaderEvent;

// no need for Manager. We use AdminManger, he knows all manager. Change in All listeners

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
