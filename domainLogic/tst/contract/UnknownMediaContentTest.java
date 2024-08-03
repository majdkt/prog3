package contract;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UnknownMediaContentTest {

    @Test
    void getAddress() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        assertEquals("unknownAddress", mediaContent.getAddress(), "Address should be 'unknownAddress'");
    }

    @Test
    void getTags() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        Collection<Tag> tags = mediaContent.getTags();
        assertNotNull(tags, "Tags should not be null");
        assertTrue(tags.isEmpty(), "Tags should be empty");
    }

    @Test
    void getSize() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        assertEquals(0, mediaContent.getSize(), "Size should be 0");
    }

    @Test
    void setAccessCount() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        mediaContent.setAccessCount(10);
        assertEquals(0, mediaContent.getAccessCount(), "Access count should remain 0 because it is not implemented");
    }

    @Test
    void getAccessCount() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        assertEquals(0, mediaContent.getAccessCount(), "Access count should be 0");
    }

    @Test
    void getUploader() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        assertNull(mediaContent.getUploader(), "Uploader should be null");
    }

    @Test
    void getAvailability() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        assertEquals(Duration.ZERO, mediaContent.getAvailability(), "Availability should be Duration.ZERO");
    }

    @Test
    void getCost() {
        UnknownMediaContent mediaContent = new UnknownMediaContent();
        assertEquals(BigDecimal.ZERO, mediaContent.getCost(), "Cost should be BigDecimal.ZERO");
    }
}
