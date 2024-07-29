package eventSystem.listeners;

import domainLogic.Manager;
import all.JbpCommands;
import eventSystem.Event;
import eventSystem.events.SaveStateJBPEvent;


import java.io.IOException;

public class SaveStateJBPEventListener implements EventListener {
    private Manager manager;
    private JbpCommands jbpCommands = new JbpCommands();

    public SaveStateJBPEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof SaveStateJBPEvent) {
            try {
                jbpCommands.saveState(manager);
                System.out.println("State saved successfully using JBP.");
            } catch (IOException e) {
                System.out.println("Failed to save state: " + e.getMessage());
            }
        }
    }
}

