package domainLogic;

import contract.Tag;
import contract.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

 class AudioImplTests {

    private AudioImpl audio;
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
        tags.add(Tag.Animal);

        audio = new AudioImpl(44100, "testAddress", tags, 100, 1_000_000, uploader, Duration.ofDays(10), BigDecimal.valueOf(4.99));
    }

    @Test
    public void testGetSamplingRate() {
        assertEquals(44100, audio.getSamplingRate());
    }

    @Test
    public void testGetAddress() {
        assertEquals("testAddress", audio.getAddress());
    }

    @Test
    public void testGetTags() {
        assertEquals(tags, audio.getTags());
    }

    @Test
    public void testGetAccessCount() {
        assertEquals(100, audio.getAccessCount());
    }

    @Test
    public void testSetAccessCount() {
        audio.setAccessCount(200);
        assertEquals(200, audio.getAccessCount());
    }

    @Test
    public void testGetSize() {
        assertEquals(1_000_000, audio.getSize());
    }

    @Test
    public void testGetUploader() {
        assertEquals(uploader, audio.getUploader());
    }

    @Test
    public void testGetAvailability() {
        // The availability is the time elapsed since the audio was uploaded
        Duration expectedDuration = Duration.between(audio.getUploadDate(), LocalDateTime.now());
        assertTrue(expectedDuration.minus(audio.getAvailability()).isZero());
    }

    @Test
    public void testGetCost() {
        assertEquals(BigDecimal.valueOf(4.99), audio.getCost());
    }

    @Test
    public void testToString() {
        String expectedString = String.format(
                "Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                "testAddress", 1_000_000 / 1_000_000.0, 44100, 100, "TestUploader",
                audio.getAvailability().toDays(), BigDecimal.valueOf(4.99), tags);
        assertEquals(expectedString, audio.toString());
    }
}
