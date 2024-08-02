package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.ReadByUploaderEvent;
import eventSystem.events.StateUpdatedEvent;

public class ReadByUploaderEventListener implements EventListener {
    private Manager manager;

    public ReadByUploaderEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof ReadByUploaderEvent) {
            System.out.println(manager.readByUploader());
        } else if (event instanceof StateUpdatedEvent) {
            this.manager = ((StateUpdatedEvent) event).getNewManager();
        }
    }
}
