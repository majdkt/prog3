package contract;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

public interface MediaContent {
    String getAddress();
    Collection<Tag> getTags();
    long getAccessCount();
    long getSize();
    Uploader getUploader();
    Duration getAvailability();
    BigDecimal getCost();

    void setAccessCount(long newAccessCount);
}
