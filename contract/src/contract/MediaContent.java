package contract;

import java.util.Collection;

public interface MediaContent {
    String getAddress();
    Collection<Tag> getTags();
    long getAccessCount();
    long getSize();
    void setAccessCount(long newAccessCount);
    Uploader getUploader();
}
