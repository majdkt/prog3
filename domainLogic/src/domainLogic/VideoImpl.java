package domainLogic;

import contract.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

public class VideoImpl implements Video {
    private Uploader uploader;
    private Duration availability;
    private BigDecimal cost;
    private String address;
    private Collection<Tag> tags;
    private long accessCount;
    private long size;
    private int resolution;

    public VideoImpl(Uploader uploader, Duration availability, BigDecimal cost, String address, Collection<Tag> tags, long size, int resolution) {
        this.uploader = uploader;
        this.availability = availability;
        this.cost = cost;
        this.address = address;
        this.tags = tags;
        this.size = size;
        this.resolution = resolution;
        this.accessCount = 0;
    }

    @Override
    public Uploader getUploader() {
        return uploader;
    }

    @Override
    public Duration getAvailability() {
        return availability;
    }

    @Override
    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Collection<Tag> getTags() {
        return tags;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    @Override
    public int getResolution() {
        return resolution;
    }
}
