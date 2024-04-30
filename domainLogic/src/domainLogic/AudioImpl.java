package domainLogic;

import contract.Audio;
import contract.Tag;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

 class AudioImpl implements Audio {
    private long accessCount = 0;
    private int samplingRate;
    private String adress;
    private long size;

     // Constructor to initialize the AudioImpl object with a sampling rate
     AudioImpl(int samplingRate, String adress, long size) {
         this.samplingRate = samplingRate;
         this.adress = adress;
         this.size = size;
     }

    @Override
    public int getSamplingRate() {return this.samplingRate;}

    @Override
    public String getAddress() {return this.adress;}

    @Override
    public Collection<Tag> getTags() {return List.of();
    }

    @Override
    public long getAccessCount() {return this.accessCount++;} // Solution for now to count each time we call the method

    @Override
    public long getSize() {return this.size;
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
