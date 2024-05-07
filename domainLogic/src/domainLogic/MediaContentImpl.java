package domainLogic;

import contract.MediaContent;
import contract.Tag;

import java.util.Collection;
import java.util.List;

public class MediaContentImpl implements MediaContent {
    String adress;
    List<Tag> tags;
    long accessCount = 0;
    long size = 0;

    public MediaContentImpl(String adress, Tag tag, long accessCount, long size) {
        this.adress = adress;
        this.tags = tags;
        this.accessCount = accessCount;
        this.size = size;
    }

    @Override
    public String getAddress() {return this.adress;}

    @Override
    public Collection<Tag> getTags() {return List.of();}

    @Override
    public long getAccessCount() {return this.accessCount++;}

    @Override
    public long getSize() {return this.size;}
}
