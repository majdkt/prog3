package domainLogic;

import contract.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

public class AudioVideoImpl implements Audio, Video, MediaContent, Serializable {
    private final int samplingRate;
    private final String address;
    private final Set<Tag> tags;
    private long accessCount;
    private final long size;
    private final UploaderImpl uploader;
    private final Duration availability;
    private final BigDecimal cost;
    private final int resolution;

    // Constructor
    public AudioVideoImpl(int samplingRate, String address, Set<Tag> tags, long accessCount, long size, UploaderImpl uploader, Duration availability, BigDecimal cost, int resolution) {
        this.samplingRate = samplingRate;
        this.address = address;
        this.tags = tags;
        this.accessCount = accessCount;
        this.size = size;
        this.uploader = uploader;
        this.availability = availability;
        this.cost = cost;
        this.resolution = resolution;
    }

    // Implementations for Audio interface
    @Override
    public int getSamplingRate() {
        return samplingRate;
    }

    // Implementations for Video interface
    @Override
    public int getResolution() {
        return resolution;
    }

    // Implementations for MediaContent interface
    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Set<Tag> getTags() {
        return tags;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    @Override
    public void setAddress(String address) {

    }

    @Override
    public long getSize() {
        return size;
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
}
