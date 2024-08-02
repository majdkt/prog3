package domainLogic;

import contract.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VideoImplTests {

    private Set<Tag> defaultTags;
    private BigDecimal defaultCost;
    private int defaultResolution;
    private Duration defaultAvailability;
    private String uploaderName;

    @BeforeEach
    void setUp() {
        defaultTags = new HashSet<>();
        defaultTags.add(Tag.News);
        defaultCost = BigDecimal.valueOf(10);
        defaultResolution = 1080;
        defaultAvailability = Duration.ofDays(0);
        uploaderName = "testUploader";
    }

    @Test
    void testVideoImplGetResolution() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultResolution, video.getResolution(), "Resolution should match the default value");
    }

    @Test
    void testVideoImplGetAddress() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals("address1", video.getAddress(), "Address should match the provided value");
    }

    @Test
    void testVideoImplGetTags() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultTags, video.getTags(), "Tags should match the provided value");
    }

    @Test
    void testVideoImplGetAccessCount() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(0, video.getAccessCount(), "Initial access count should be 0");
    }

    @Test
    void testVideoImplGetSize() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(10_000_000, video.getSize(), "Size should match the provided value");
    }

    @Test
    void testVideoImplGetUploader() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(uploaderName, video.getUploader().getName(), "Uploader name should match the provided value");
    }



    @Test
    void testVideoImplGetCost() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultCost, video.getCost(), "Cost should match the provided value");
    }

}
