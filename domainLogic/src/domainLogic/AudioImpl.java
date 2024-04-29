package domainLogic;

import contract.Audio;
import contract.Tag;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

 class AudioImpl implements Audio {
    private long accessCount;
    private int samplingRate;
    private String adress;
    private long size;

     // Constructor to initialize the AudioImpl object with a sampling rate
     public AudioImpl(int samplingRate, String adress, long size) {
         this.samplingRate = samplingRate;
         this.adress = adress;
         this.size = size;
     }

    @Override
    public int getSamplingRate() {
        // Return the stored sampling rate
        return this.samplingRate;
    }

    @Override
    public String getAddress() {
        return this.adress;
    }

    @Override
    public Collection<Tag> getTags() {
        return List.of();
    }

    protected void setAccessCount(long value){
        this.accessCount=value;
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
        return null;
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
