package eventSystem.listeners;

import commands.JbpCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.EventDispatcher;
import eventSystem.events.LoadStateJBPEvent;
import eventSystem.events.StateUpdatedEvent;

import java.io.IOException;


public class LoadStateJBPEventListener implements EventListener {
    private EventDispatcher eventDispatcher;
    private Manager manager;
    private final JbpCommands jbpCommands = new JbpCommands();

    public LoadStateJBPEventListener(EventDispatcher eventDispatcher, Manager manager) {
        this.eventDispatcher = eventDispatcher;
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof LoadStateJBPEvent) {
            try {
                Manager loadedManager = jbpCommands.loadStateJBP();
                if (loadedManager != null) {
                    manager = loadedManager;
                    eventDispatcher.dispatch(new StateUpdatedEvent(loadedManager));
                    System.out.println("State loaded successfully using JBP.");
                } else {
                    System.out.println("Failed to load state or no state was saved using JBP.");
                }
            } catch (IOException e) {
                System.out.println("Failed to load state using JBP: " + e.getMessage());
            }
        }
    }
}
