package contract;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;

// Dummy class to simulate unknown MediaContent type
public class UnknownMediaContent implements MediaContent {
    @Override
    public String getAddress() {
        return "unknownAddress";
    }

    @Override
    public Collection<Tag> getTags() {
        return Collections.emptyList();
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public void setAccessCount(long newAccessCount) {

    }

    @Override
    public long getAccessCount() {
        return 0;
    }

    @Override
    public Uploader getUploader() {
        return null;
    }

    public Duration getAvailability() {
        return Duration.ZERO;
    }

    public BigDecimal getCost() {
        return BigDecimal.ZERO;
    }

}