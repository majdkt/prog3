package domainLogic;

import contract.Audio;
import contract.MediaContent;
import contract.Tag;
import contract.Uploader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public class AudioImpl implements Audio, Serializable, MediaContent {
    private static final long serialVersionUID = 1L;
    private int samplingRate;
    private String address;
    private Collection<Tag> tags;
    private long accessCount;
    private long size;
    private Uploader uploader;
    private Duration availability;
    private BigDecimal cost;

    public AudioImpl(int randomSamplingRate, String address, Set<Tag> tags, int i, long size, UploaderImpl uploader, Duration randomAvailability, BigDecimal randomCost, LocalDateTime uploadDate) {
        // Default constructor
    }

    public AudioImpl(int samplingRate, String address, Collection<Tag> tags,
                     long accessCount, long size, Uploader uploader, Duration availability, BigDecimal cost) {
        this.samplingRate = samplingRate;
        this.address = address;
        this.tags = tags;
        this.accessCount = accessCount;
        this.size = size;
        this.uploader = uploader;
        this.availability = availability;
        this.cost = cost;
    }

    @Override
    public int getSamplingRate() {
        return samplingRate;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Collection<Tag> getTags() {
        return tags;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long newAccessCount) {
        this.accessCount = newAccessCount;
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
