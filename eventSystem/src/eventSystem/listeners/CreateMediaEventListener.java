package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.CreateMediaEvent;
import eventSystem.events.StateUpdatedEvent;

public class CreateMediaEventListener implements EventListener {
    private Manager manager;

    public CreateMediaEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof CreateMediaEvent) {
            CreateMediaEvent createMediaEvent = (CreateMediaEvent) event;
            // Ensure uploader exists before creating media
                manager.create(
                        createMediaEvent.getUploaderName(),
                        createMediaEvent.getMediaType(),
                        createMediaEvent.getTags(),
                        createMediaEvent.getSize(),
                        createMediaEvent.getCost(),
                        createMediaEvent.getSamplingRate(),
                        createMediaEvent.getResolution(),
                        createMediaEvent.getAvailability()
                );
            } else if (event instanceof StateUpdatedEvent) {
            this.manager = ((StateUpdatedEvent) event).getNewManager();
        }
        }
}
