package events;

import contract.Tag;

import java.math.BigDecimal;
import java.util.Set;

public class MediaUploadedEvent extends Event {
    private final String uploaderName;
    private final String mediaType;
    private final Set<Tag> tags;
    private final long size;
    private final BigDecimal cost;

    public MediaUploadedEvent(String uploaderName, String mediaType, Set<Tag> tags, long size, BigDecimal cost) {
        this.uploaderName = uploaderName;
        this.mediaType = mediaType;
        this.tags = tags;
        this.size = size;
        this.cost = cost;
    }

    // Getters
    public String getUploaderName() { return uploaderName; }
    public String getMediaType() { return mediaType; }
    public Set<Tag> getTags() { return tags; }
    public long getSize() { return size; }
    public BigDecimal getCost() { return cost; }
}

