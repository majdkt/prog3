package domainLogic;

import contract.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;

public class VideoImpl implements Video, Serializable, MediaContent {
    private Uploader uploader;
    private Duration availability;
    private BigDecimal cost;
    private String address;
    private Collection<Tag> tags;
    private long accessCount;
    private long size;
    private int resolution;

    public VideoImpl(Uploader uploader, Set<Tag> tags, String address, long size, Duration availability, BigDecimal cost, int additionalAttribute) {

        this.uploader = uploader;
        this.availability = this.availability;
        this.cost = this.cost;
        this.address = this.address;
        this.tags = this.tags;
        this.size = this.size;
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
    public int getResolution() {
        return resolution;
    }

    @Override
    public void setAccessCount(long newAccessCount) {
        this.accessCount = accessCount;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}