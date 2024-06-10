package domainLogic;

import contract.Audio;
import contract.Tag;
import contract.Uploader;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

class AudioImpl implements Audio, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int samplingRate;
    private String address;
    private Collection<Tag> tags;
    private long accessCount;
    private long size;
    private Uploader uploader;
    private Duration availability;
    private BigDecimal cost;
    public AudioImpl() {
        this.accessCount = 0;
    }
    public AudioImpl(String address) {
        this.address = address;
        this.accessCount = 0;
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
    @Override
    public String toString() {
        return "AudioFile{address='" + address + "', accessCount=" + accessCount + "}";
    }
}