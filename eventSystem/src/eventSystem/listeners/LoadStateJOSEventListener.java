package eventSystem.listeners;

import commands.JosCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.EventDispatcher;
import eventSystem.events.LoadStateJOSEvent;
import eventSystem.events.StateUpdatedEvent;

import java.io.IOException;

public class LoadStateJOSEventListener implements EventListener {
    private EventDispatcher eventDispatcher;
    private Manager manager;
    private final JosCommands josCommands = new JosCommands();

    public LoadStateJOSEventListener(EventDispatcher eventDispatcher, Manager manager) {
        this.eventDispatcher = eventDispatcher;
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof LoadStateJOSEvent) {
            try {
                Manager loadedManager = josCommands.loadState();
                if (loadedManager != null) {
                    manager = loadedManager;
                    eventDispatcher.dispatch(new StateUpdatedEvent(loadedManager));
                    System.out.println("State loaded successfully.");
                } else {
                    System.out.println("Failed to load state or no state was saved.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load state: " + e.getMessage());
            }
        }
    }
}
