package domainLogic;

import contract.Tag;
import contract.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

 class AudioVideoImplTests {

    private AudioVideoImpl audioVideo;
    private Uploader uploader;
    private Set<Tag> tags;

    @BeforeEach
    public void setUp() {
        String uploaderName = "test";
        tags = new HashSet<>();
        tags.add(Tag.Animal);
        uploader = new UploaderImpl(uploaderName);
        audioVideo = new AudioVideoImpl(44100, "testAddress", tags, 100, 1_000_000,
                new UploaderImpl(uploader.getName()), Duration.ofDays(10),
                BigDecimal.valueOf(5.99), 1080);
    }

    @Test
    public void testGetSamplingRate() {
        assertEquals(44100, audioVideo.getSamplingRate());
    }

    @Test
    public void testGetResolution() {
        assertEquals(1080, audioVideo.getResolution());
    }

    @Test
    public void testGetAddress() {
        assertEquals("testAddress", audioVideo.getAddress());
    }

    @Test
    public void testGetTags() {
        assertEquals(tags, audioVideo.getTags());
    }

    @Test
    public void testGetAccessCount() {
        assertEquals(100, audioVideo.getAccessCount());
    }

    @Test
    public void testSetAccessCount() {
        audioVideo.setAccessCount(200);
        assertEquals(200, audioVideo.getAccessCount());
    }

    @Test
    public void testGetSize() {
        assertEquals(1_000_000, audioVideo.getSize());
    }


    @Test
    public void testGetAvailability() {
        // The availability is the time elapsed since the audio/video was uploaded
        Duration expectedDuration = Duration.between(audioVideo.getUploadDate(), LocalDateTime.now());
        assertTrue(expectedDuration.minus(audioVideo.getAvailability()).isZero());
    }

    @Test
    public void testGetCost() {
        assertEquals(BigDecimal.valueOf(5.99), audioVideo.getCost());
    }

    @Test
    public void testToString() {
        String expectedString = String.format(
                "AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                "testAddress", 1_000_000 / 1_000_000.0, 44100, 1080, 100, "test",
                audioVideo.getAvailability().toDays(), BigDecimal.valueOf(5.99), tags);
        assertEquals(expectedString, audioVideo.toString());
    }

}
