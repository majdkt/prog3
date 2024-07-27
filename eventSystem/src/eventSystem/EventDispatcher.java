package eventSystem;

import eventSystem.listeners.MediaListener;
import eventSystem.listeners.UploaderListener;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {
    private final List<UploaderListener> uploaderListeners = new ArrayList<>();
    private final List<MediaListener> mediaListeners = new ArrayList<>();

    public void addUploaderListener(UploaderListener listener) {
        uploaderListeners.add(listener);
    }

    public void removeUploaderListener(UploaderListener listener) {
        uploaderListeners.remove(listener);
    }

    public void addMediaListener(MediaListener listener) {
        mediaListeners.add(listener);
    }

    public void removeMediaListener(MediaListener listener) {
        mediaListeners.remove(listener);
    }

    public void fireUploaderCreated(UploaderEvent event) {
        for (UploaderListener listener : uploaderListeners) {
            listener.uploaderCreated(event);
        }
    }

    public void fireUploaderDeleted(UploaderEvent event) {
        for (UploaderListener listener : uploaderListeners) {
            listener.uploaderDeleted(event);
        }
    }

    public void fireMediaCreated(MediaEvent event) {
        for (MediaListener listener : mediaListeners) {
            listener.mediaCreated(event);
        }
    }

    public void fireMediaDeleted(MediaEvent event) {
        for (MediaListener listener : mediaListeners) {
            listener.mediaDeleted(event);
        }
    }
}
