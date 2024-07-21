package contract;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

public interface MediaContent {
    String getAddress();
    Collection<Tag> getTags();
    long getAccessCount();
    long getSize();

    void setAccessCount(long newAccessCount);

    void setAddress(String address);
}
