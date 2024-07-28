package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.CreateUploaderEvent;
import eventSystem.events.DeleteEvent;
import eventSystem.events.ReadByUploaderEvent;
import eventSystem.events.ReadContentEvent;

public class UploaderListener implements EventListener {
    private final Manager manager;

    public UploaderListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof CreateUploaderEvent) {
            manager.createUploader(((CreateUploaderEvent) event).getUploaderName());
        } else if (event instanceof DeleteEvent) {
            manager.deleteUploader(((DeleteEvent) event).getTarget());
        } else if (event instanceof ReadByUploaderEvent) {
            manager.readByUploader();
        } else if (event instanceof ReadContentEvent) {
            manager.read();
        }
    }
}
