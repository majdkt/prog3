package eventSystem.listeners;

import eventSystem.MediaEvent;

import java.util.EventListener;

public interface MediaListener extends EventListener {
    void mediaCreated(MediaEvent event);
    void mediaDeleted(MediaEvent event);
}
