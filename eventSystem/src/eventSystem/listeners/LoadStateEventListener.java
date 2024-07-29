package eventSystem.listeners;

import all.JosCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.LoadStateEvent;

import java.io.IOException;

public class LoadStateEventListener implements EventListener {
    private Manager manager;
    private final JosCommands josCommands = new JosCommands(); // Instance of JosCommands

    public LoadStateEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof LoadStateEvent) {
            try {
                Manager loadedManager = josCommands.loadState();
                if (loadedManager != null) {
                    this.manager = loadedManager;
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
