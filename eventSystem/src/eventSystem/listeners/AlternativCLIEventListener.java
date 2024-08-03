package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.AlternativCLIEvent;


public class AlternativCLIEventListener implements EventListener {
    private Manager manager;

    public AlternativCLIEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof AlternativCLIEvent) {
            System.out.println("Sorry, this feature is not available in Alternativ CLI");
        }
    }
}
