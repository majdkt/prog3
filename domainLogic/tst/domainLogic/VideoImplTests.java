package domainLogic;

import contract.MediaContent;
import contract.Tag;
import contract.Uploader;
import contract.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoImplTests {

    private VideoImpl video;
    private Uploader uploader;
    private Collection<Tag> tags;

    @BeforeEach
    public void setUp() {
        uploader = new Uploader() {
            @Override
            public String getName() {
                return "TestUploader";
            }
        };

        tags = new ArrayList<>();
        tags.add(Tag.News);

        video = new VideoImpl("testAddress", tags, 100, 1_000_000, uploader, Duration.ofDays(10), BigDecimal.valueOf(9.99), 1080);
    }

    @Test
    public void testGetUploader() {
        assertEquals(uploader, video.getUploader());
    }

    @Test
    public void testGetAvailability() {
        // The availability is the time elapsed since the video was uploaded
        Duration expectedDuration = Duration.between(video.uploadDate, LocalDateTime.now());
        assertTrue(expectedDuration.minus(video.getAvailability()).isZero());
    }

    @Test
    public void testGetCost() {
        assertEquals(BigDecimal.valueOf(9.99), video.getCost());
    }

    @Test
    public void testGetAddress() {
        assertEquals("testAddress", video.getAddress());
    }

    @Test
    public void testGetTags() {
        assertEquals(tags, video.getTags());
    }

    @Test
    public void testGetAccessCount() {
        assertEquals(100, video.getAccessCount());
    }

    @Test
    public void testGetSize() {
        assertEquals(1_000_000, video.getSize());
    }

    @Test
    public void testGetResolution() {
        assertEquals(1080, video.getResolution());
    }

    @Test
    public void testSetAccessCount() {
        video.setAccessCount(200);
        assertEquals(200, video.getAccessCount());
    }

    @Test
    public void testToString() {
        String expectedString = String.format(
                "Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                "testAddress", 1_000_000 / 1_000_000.0, 1080, 100, "TestUploader",
                video.getAvailability().toDays(), BigDecimal.valueOf(9.99), tags);
        assertEquals(expectedString, video.toString());
    }
}
