package eventSystem.listeners;

import contract.Tag;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.ReadUsedTagsEvent;
import eventSystem.events.ReadUnusedTagsEvent;
import eventSystem.events.StateUpdatedEvent;

import java.util.Map;

public class ReadUsedTagsEventListener implements EventListener {
    private Manager manager;

    public ReadUsedTagsEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof ReadUsedTagsEvent) {
            Map<Tag, Boolean> tagStatus = manager.readByTag();
            tagStatus.entrySet().stream()
                    .filter(Map.Entry::getValue) // Filter only true tags
                    .forEach(entry -> System.out.println(entry.getKey()));
        } else if (event instanceof StateUpdatedEvent) {
            this.manager = ((StateUpdatedEvent) event).getNewManager();
        }
    }
}

