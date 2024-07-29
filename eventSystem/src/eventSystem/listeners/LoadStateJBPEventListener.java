package eventSystem.listeners;

import all.JbpCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.LoadStateJBPEvent;

import java.io.IOException;

public class LoadStateJBPEventListener implements EventListener {
    private Manager manager;
    private JbpCommands jbpCommands = new JbpCommands();

    public LoadStateJBPEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof LoadStateJBPEvent) {
            try {
                Manager loadedManager = jbpCommands.loadState();
                if (loadedManager != null) {
                    this.manager = loadedManager;
                    System.out.println("State loaded successfully using JBP.");
                } else {
                    System.out.println("Failed to load state or no state was saved.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load state: " + e.getMessage());
            }
        }
    }
}
