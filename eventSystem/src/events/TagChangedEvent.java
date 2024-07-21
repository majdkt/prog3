package events;

import contract.Tag;

import java.util.Set;

public class TagChangedEvent extends Event {
    private final Set<Tag> tags;

    public TagChangedEvent(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Tag> getTags() { return tags; }
}
