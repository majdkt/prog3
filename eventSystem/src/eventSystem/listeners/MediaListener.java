package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.*;


public class MediaListener implements EventListener {
    private final Manager manager;

    public MediaListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof CreateMediaEvent) {
            CreateMediaEvent createEvent = (CreateMediaEvent) event;
            manager.create(createEvent.getUploaderName(), createEvent.getMediaType(), createEvent.getTags(),
                    createEvent.getSize(), createEvent.getCost(), createEvent.getSamplingRate(), createEvent.getResolution(),
                    createEvent.getAvailability());
        } else if (event instanceof DeleteEvent) {
            manager.deleteMedia(((DeleteEvent) event).getTarget());
        } else if (event instanceof ReadByTagEvent) {
            manager.readByTag();
        } else if (event instanceof ReadByMediaTypeEvent) {
            manager.readByMediaType(((ReadByMediaTypeEvent) event).getMediaType());
        } else if (event instanceof UpdateAccessCountEvent) {
            manager.updateAccessCount(((UpdateAccessCountEvent) event).getMediaAddress());
        } else if (event instanceof SaveStateEvent) {
            //manager.saveState;
        } else if (event instanceof LoadStateEvent) {
            //manager.loadState();
        }
    }
}
