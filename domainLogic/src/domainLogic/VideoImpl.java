package domainLogic;

import contract.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class VideoImpl implements Video {
    private int resolution;
    private String address;
    private long accessCount;
    private long size;
    private Uploader uploader;

    public VideoImpl(int resolution, String address, long size, Uploader uploader) {
        this.resolution = resolution;
        this.address = address;
        this.size = size;
        this.uploader = uploader;
    }

    public void updateResolution(int newResolution) {
        this.resolution = newResolution;
    }

    @Override
    public int getResolution() {
        return this.resolution;
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
        return this.accessCount;
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
        // Implement if needed
        return null;
    }

    @Override
    public BigDecimal getCost() {
        // Implement if needed
        return null;
    }
}
