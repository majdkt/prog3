package eventSystem.events;

import contract.Tag;
import eventSystem.Event;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

public class CreateMediaEvent implements Event {
    private final String uploaderName;
    private final String mediaType;
    private final Set<Tag> tags;
    private final long size;
    private final BigDecimal cost;
    private final int samplingRate;
    private final int resolution;
    private final Duration availability;

    public CreateMediaEvent(String uploaderName, String mediaType, Set<Tag> tags, long size, BigDecimal cost, int samplingRate, int resolution, Duration availability) {
        this.uploaderName = uploaderName;
        this.mediaType = mediaType;
        this.tags = tags;
        this.size = size;
        this.cost = cost;
        this.samplingRate = samplingRate;
        this.resolution = resolution;
        this.availability = availability;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public long getSize() {
        return size;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public int getResolution() {
        return resolution;
    }

    public Duration getAvailability() {
        return availability;
    }
}
