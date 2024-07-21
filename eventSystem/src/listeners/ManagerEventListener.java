package listeners;

import events.*;

public class ManagerEventListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        if (event instanceof MediaUploadedEvent) {
            MediaUploadedEvent e = (MediaUploadedEvent) event;
            System.out.println("Media uploaded: " + e.getUploaderName() + ", " + e.getMediaType());
        } else if (event instanceof MediaDeletedEvent) {
            MediaDeletedEvent e = (MediaDeletedEvent) event;
            System.out.println("Media deleted: " + e.getAddress());
        } else if (event instanceof UploaderDeletedEvent) {
            UploaderDeletedEvent e = (UploaderDeletedEvent) event;
            System.out.println("Uploader deleted: " + e.getUploaderName());
        } else if (event instanceof AccessCountUpdatedEvent) {
            AccessCountUpdatedEvent e = (AccessCountUpdatedEvent) event;
            System.out.println("Access count updated for media: " + e.getAddress());
        } else if (event instanceof TagChangedEvent) {
            TagChangedEvent e = (TagChangedEvent) event;
            System.out.println("Tags changed: " + e.getTags());
        }
    }
}
