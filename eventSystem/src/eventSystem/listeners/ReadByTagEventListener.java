package eventSystem.listeners;

import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.ReadByTagEvent;

public class ReadByTagEventListener implements EventListener {
    private Manager manager;

    public ReadByTagEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof ReadByTagEvent) {
            System.out.println(manager.readByTag());
        }
    }
}
