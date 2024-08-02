package domainLogic;

import contract.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AudioImplTests {

    private Set<Tag> defaultTags;
    private BigDecimal defaultCost;
    private int defaultSamplingRate;
    private Duration defaultAvailability;
    private String uploaderName;

    @BeforeEach
    void setUp() {
        defaultTags = new HashSet<>();
        defaultTags.add(Tag.News);
        defaultCost = BigDecimal.valueOf(10);
        defaultSamplingRate = 44100;
        defaultAvailability = Duration.ofDays(0);
        uploaderName = "testUploader";
    }

    @Test
    void testAudioImplGetSamplingRate() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(defaultSamplingRate, audio.getSamplingRate(), "Sampling rate should match the default value");
    }

    @Test
    void testAudioImplGetAddress() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals("address1", audio.getAddress(), "Address should match the provided value");
    }

    @Test
    void testAudioImplGetTags() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(defaultTags, audio.getTags(), "Tags should match the provided value");
    }

    @Test
    void testAudioImplGetAccessCount() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(0, audio.getAccessCount(), "Initial access count should be 0");
    }

    @Test
    void testAudioImplGetSize() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(5_000_000, audio.getSize(), "Size should match the provided value");
    }

    @Test
    void testAudioImplGetUploader() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(uploaderName, audio.getUploader().getName(), "Uploader name should match the provided value");
    }

    @Test
    void testAudioImplGetAvailabilityPeriod() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(defaultAvailability, audio.getAvailability(), "Availability period should match the provided value");
    }

    @Test
    void testAudioImplGetUploadTime() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertNotNull(audio.getUploadDate(), "Upload time should not be null");
        assertTrue(audio.getUploadDate().isBefore(LocalDateTime.now()), "Upload time should be before the current time");
    }

    @Test
    void testAudioImplGetCost() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(defaultCost, audio.getCost(), "Cost should match the provided value");
    }

    @Test
    void testAudioImplIncrementAccessCount() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        audio.setAccessCount(audio.getAccessCount()+1);
        assertEquals(1, audio.getAccessCount(), "Access count should increment by 1");
    }
}
