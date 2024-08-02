package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.ReadByMediaTypeEvent;
import eventSystem.events.StateUpdatedEvent;

public class ReadByMediaTypeEventListener implements EventListener {
    private Manager manager;

    public ReadByMediaTypeEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof ReadByMediaTypeEvent) {
            ReadByMediaTypeEvent readByMediaTypeEvent = (ReadByMediaTypeEvent) event;
            System.out.println(manager.readByMediaType(readByMediaTypeEvent.getMediaType()));
        } else if (event instanceof StateUpdatedEvent) {
            this.manager = ((StateUpdatedEvent) event).getNewManager();
        }
    }
}
