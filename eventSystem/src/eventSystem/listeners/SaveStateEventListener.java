package eventSystem.listeners;

import all.JosCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.SaveStateJOSEvent;

import java.io.IOException;

public class SaveStateEventListener implements EventListener {
    private Manager manager;
    private final JosCommands josCommands = new JosCommands(); // Instance of JosCommands

    public SaveStateEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof SaveStateJOSEvent) {
            try {
                josCommands.saveState(manager);
                System.out.println("State saved successfully.");
            } catch (IOException e) {
                System.out.println("Failed to save state: " + e.getMessage());
            }
        }
    }
}
