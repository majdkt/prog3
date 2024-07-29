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
            CreateMediaEvent createMediaEvent = (CreateMediaEvent) event;
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
        } else if (event instanceof ReadContentEvent) {
            System.out.println(manager.read());
        } else if (event instanceof ReadByTagEvent) {
            System.out.println(manager.readByTag());
        } else if (event instanceof ReadByUploaderEvent) {
            System.out.println(manager.readByUploader());
        } else if (event instanceof ReadByMediaTypeEvent) {
            ReadByMediaTypeEvent readByMediaTypeEvent = (ReadByMediaTypeEvent) event;
            System.out.println(manager.readByMediaType(readByMediaTypeEvent.getMediaType()));
        } else if (event instanceof UpdateAccessCountEvent) {
            UpdateAccessCountEvent updateAccessCountEvent = (UpdateAccessCountEvent) event;
            manager.updateAccessCount(updateAccessCountEvent.getAddress());
        } else if (event instanceof DeleteEvent) {
            DeleteEvent deleteEvent = (DeleteEvent) event;
            manager.deleteMedia(deleteEvent.getTarget());
        } else if (event instanceof DeleteUploaderEvent) {
            DeleteUploaderEvent deleteUploaderEvent = (DeleteUploaderEvent) event;
            manager.deleteUploader(deleteUploaderEvent.getUploaderName());
        } else if (event instanceof SaveStateEvent) {
            //manager.saveState();
        } else if (event instanceof LoadStateEvent) {
            //manager.loadState();
        } else if (event instanceof CheckUploaderExistenceEvent) {
            CheckUploaderExistenceEvent checkUploaderExistenceEvent = (CheckUploaderExistenceEvent) event;
            boolean exists = manager.uploaderExists(checkUploaderExistenceEvent.getUploaderName());
            checkUploaderExistenceEvent.setExists(exists);
        }
    }
}
