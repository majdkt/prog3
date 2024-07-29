package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.ReadContentEvent;

public class ReadContentEventListener implements EventListener {
    private Manager manager;

    public ReadContentEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof ReadContentEvent) {
            System.out.println(manager.read());
        }
    }
}
