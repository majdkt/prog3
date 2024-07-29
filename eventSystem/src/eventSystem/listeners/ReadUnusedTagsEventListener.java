package eventSystem.listeners;

import contract.Tag;
import domainLogic.Manager;
import eventSystem.Event;
import eventSystem.events.ReadUnusedTagsEvent;

import java.util.Map;

public class ReadUnusedTagsEventListener implements EventListener {
    private Manager manager;

    public ReadUnusedTagsEventListener(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof ReadUnusedTagsEvent) {
            Map<Tag, Boolean> tagStatus = manager.readByTag();
            tagStatus.entrySet().stream()
                    .filter(entry -> !entry.getValue()) // Filter only false tags
                    .forEach(entry -> System.out.println(entry.getKey()));
        }
    }
}
