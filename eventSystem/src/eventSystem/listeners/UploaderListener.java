package eventSystem.listeners;

import eventSystem.UploaderEvent;

import java.util.EventListener;

public interface UploaderListener extends EventListener {
    void uploaderCreated(UploaderEvent event);
    void uploaderDeleted(UploaderEvent event);
}

