package domainLogic;

import contract.MediaContent;
import contract.Tag;
import contract.Uploader;
import contract.Video;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private LocalDateTime uploadDate;


    public VideoImpl(String address, Collection<Tag> tags, long accessCount, long size,
                     Uploader uploader, Duration availability, BigDecimal cost, int resolution) {
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
        this.accessCount = newAccessCount;
    }

}
