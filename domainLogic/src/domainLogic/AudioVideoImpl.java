package domainLogic;

import contract.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Set;

public class AudioVideoImpl implements Audio, Video, MediaContent, Serializable, AudioVideo {
    private final int samplingRate;
    private String address;
    private final Set<Tag> tags;
    private long accessCount;
    private final long size;
    private final UploaderImpl uploader;
    private final Duration availability;
    private final BigDecimal cost;
    private final int resolution;
    private LocalDateTime uploadDate;


    public AudioVideoImpl(int samplingRate, String address, Set<Tag> tags, int accessCount, long size, UploaderImpl uploader, Duration availability, BigDecimal cost, int resolution) {
        this.samplingRate = samplingRate;
        this.address = address;
        this.tags = tags;
        this.accessCount = accessCount;
        this.size = size;
        this.uploader = uploader;
        this.availability = availability;
        this.cost = cost;
        this.resolution = resolution;
        this.uploadDate = LocalDateTime.now();
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
    public long getSize() {
        return size;
    }

    @Override
    public Uploader getUploader() {
        return uploader;
    }

    @Override
    public Duration getAvailability() {
        return Duration.between(uploadDate, LocalDateTime.now());
    }

    @Override
    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format(
                "AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d millis, Cost: %.2f, Tags: %s]",
                getAddress(), getSize() / 1_000_000.0, getSamplingRate(), getResolution(), getAccessCount(), getUploader().getName(),
                getAvailability().toMillis(), getCost(), getTags());
    }

    public Temporal getUploadDate() {
        return uploadDate;
    }
}
