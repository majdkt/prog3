package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.DeleteUploaderEvent;
import eventSystem.events.StateUpdatedEvent;

public class DeleteUploaderEventListener implements EventListener {
    private Manager manager;

    public DeleteUploaderEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof DeleteUploaderEvent) {
            DeleteUploaderEvent deleteUploaderEvent = (DeleteUploaderEvent) event;
            try {
                if (manager.uploaderExists(deleteUploaderEvent.getUploaderName())) {
                    manager.deleteUploader(deleteUploaderEvent.getUploaderName());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage()); // Handle the exception
            }
        } else if (event instanceof StateUpdatedEvent) {
            this.manager = ((StateUpdatedEvent) event).getNewManager();
        }
    }
}
