package eventSystem.listeners;

import all.JosCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.*;

import java.io.IOException;

public class MediaListener implements EventListener {
    private Manager manager;
    private final JosCommands josCommands = new JosCommands(); // Instance of JosCommands

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
            try {
                josCommands.saveState(manager);
                System.out.println("State saved successfully.");
            } catch (IOException e) {
                System.out.println("Failed to save state: " + e.getMessage());
            }
        } else if (event instanceof LoadStateEvent) {
            try {
                Manager loadedManager = josCommands.loadState();
                if (loadedManager != null) {
                    // Assuming you want to replace the current manager with the loaded one
                    // If not, you can update or merge state as needed
                    this.manager = loadedManager;
                    System.out.println("State loaded successfully.");
                } else {
                    System.out.println("Failed to load state or no state was saved.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load state: " + e.getMessage());
            }
        } else if (event instanceof CheckUploaderExistenceEvent) {
            CheckUploaderExistenceEvent checkUploaderExistenceEvent = (CheckUploaderExistenceEvent) event;
            boolean exists = manager.uploaderExists(checkUploaderExistenceEvent.getUploaderName());
            checkUploaderExistenceEvent.setExists(exists);
            if (exists){
                CreateMediaEvent createMediaEvent = (CreateMediaEvent) event;
                System.out.println("Adding to an existing uploader ");
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
            }
        }
    }
}
