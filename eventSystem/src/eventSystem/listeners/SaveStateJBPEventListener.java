package eventSystem.listeners;

import commands.JbpCommands;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.SaveStateJBPEvent;

import java.io.IOException;

public class SaveStateJBPEventListener implements EventListener {
    private Manager manager;
    private final JbpCommands jpbCommands = new JbpCommands();

    public SaveStateJBPEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof SaveStateJBPEvent) {
            try {
                jpbCommands.saveStateJBP(manager);
                System.out.println("State saved successfully using JBP.");
            } catch (IOException e) {
                System.out.println("Failed to save state using JBP: " + e.getMessage());
            }
        }
    }
}
