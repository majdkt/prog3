package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.DeleteUploaderEvent;

public class DeleteUploaderEventListener implements EventListener {
    private Manager manager;

    public DeleteUploaderEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof DeleteUploaderEvent) {
            DeleteUploaderEvent deleteUploaderEvent = (DeleteUploaderEvent) event;
            manager.deleteUploader(deleteUploaderEvent.getUploaderName());
        }
    }
}
