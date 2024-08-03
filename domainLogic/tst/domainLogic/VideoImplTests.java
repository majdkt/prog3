package domainLogic;

import contract.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.Duration;
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
        defaultAvailability = Duration.ofDays(30);
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
    void testVideoImplSetAccessCount() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        video.setAccessCount(100);
        assertEquals(100, video.getAccessCount(), "Access count should be updated to 100");
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

    @Test
    void testVideoImplGetAvailability() throws InterruptedException {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        Thread.sleep(1000); // To ensure some time passes after video creation
        assertTrue(video.getAvailability().getSeconds() >= 1, "Availability should be at least 1 second after creation");
    }

    @Test
    void testVideoImplToString() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        String expectedString = String.format(
                "Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                video.getAddress(), video.getSize() / 1_000_000.0, video.getResolution(), video.getAccessCount(), video.getUploader().getName(),
                video.getAvailability().toDays(), video.getCost(), video.getTags());
        assertEquals(expectedString, video.toString(), "toString output should match the expected format and values");
    }
}
