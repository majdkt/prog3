package domainLogic;

import contract.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class AudioImpl implements Audio {
    private int samplingRate;
    private String address;
    private long accessCount;
    private long size;
    private Uploader uploader;

    public AudioImpl(int samplingRate, String address, long size, Uploader uploader) {
        this.samplingRate = samplingRate;
        this.address = address;
        this.size = size;
        this.uploader = uploader;
    }

    public void updateSamplingRate(int newSamplingRate) {
        this.samplingRate = newSamplingRate;
    }

    @Override
    public int getSamplingRate() {
        return this.samplingRate;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public Collection<Tag> getTags() {
        return List.of();
    }

    @Override
    public long getAccessCount() {
        return this.accessCount++;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public Uploader getUploader() {
        return this.uploader;
    }

    @Override
    public Duration getAvailability() {
        return null;
    }

    @Override
    public BigDecimal getCost() {
        return null;
    }

}
