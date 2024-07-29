package domainLogic;

import contract.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DomainLogicTests {

    private Manager manager;
    private static final long MAX_CAPACITY = 100_000_000; // 100 MB
    private Set<Tag> defaultTags;
    private BigDecimal defaultCost;
    private int defaultSamplingRate;
    private int defaultResolution;
    private Duration defaultAvailability;
    private String uploaderName;

    @BeforeEach
    void setUp() {
        manager = new Manager(MAX_CAPACITY);
        defaultTags = new HashSet<>();
        defaultTags.add(Tag.News);
        defaultCost = BigDecimal.valueOf(10);
        defaultSamplingRate = 44100;
        defaultResolution = 1080;
        defaultAvailability = Duration.ofDays(0);
        uploaderName = "testUploader";
        manager.createUploader(uploaderName);
    }


    // Tests for Manager

    @Test
    void createAudioSuccessfully() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        assertEquals(5_000_000, manager.getCurrentTotalSize());
    }

    @Test
    void createVideoSuccessfully() {
        manager.create(uploaderName, "Video", defaultTags, 10_000_000, defaultCost, 0, defaultResolution, defaultAvailability);
        assertEquals(10_000_000, manager.getCurrentTotalSize());
    }

    @Test
    void createAudioVideoSuccessfully() {
        manager.create(uploaderName, "AudioVideo", defaultTags, 15_000_000, defaultCost, defaultSamplingRate, defaultResolution, defaultAvailability);
        assertEquals(15_000_000, manager.getCurrentTotalSize());
    }

    @Test
    void createWithInvalidMediaTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.create(uploaderName, "InvalidType", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability)
        );
    }

    @Test
    void createExceedingCapacityThrowsException() {
        manager.create(uploaderName, "Audio", defaultTags, MAX_CAPACITY, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        assertThrows(IllegalArgumentException.class, () ->
                manager.create(uploaderName, "Audio", defaultTags, 1, defaultCost, defaultSamplingRate, 0, defaultAvailability)
        );
    }

    @Test
    void deleteMediaSuccessfully() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        String address = manager.read().split("\n")[0].split(" ")[3].replace(",", "");
        manager.deleteMedia(address);
        assertFalse(manager.read().contains(address));
    }

    @Test
    void deleteMediaNotFound() {
        manager.deleteMedia("nonexistentAddress");
        assertTrue(manager.read().isEmpty());
    }

    @Test
    void createUploaderSuccessfully() {
        manager.createUploader("newUploader");
        assertTrue(manager.uploaderExists("newUploader"));
    }

    @Test
    void deleteUploaderSuccessfully() {
        manager.deleteUploader(uploaderName);
        assertFalse(manager.uploaderExists(uploaderName));
    }

    @Test
    void deleteUploaderWithMediaRemovesMedia() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        manager.deleteUploader(uploaderName);
        assertEquals(0, manager.getCurrentTotalSize());
    }

    @Test
    void readMediaDetails() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        String details = manager.read();
        assertTrue(details.contains("Audio File"));
    }

    @Test
    void readByTagWithExistingTag() {
        manager.create(uploaderName, "Video", defaultTags, 10_000_000, defaultCost, 0, defaultResolution, defaultAvailability);
        Map<Tag, Boolean> tags = manager.readByTag();
        assertTrue(tags.get(Tag.News));
    }

    @Test
    void readByTagWithNonExistingTag() {
        manager.create(uploaderName, "Video", defaultTags, 10_000_000, defaultCost, 0, defaultResolution, defaultAvailability);
        Map<Tag, Boolean> tags = manager.readByTag();
        assertFalse(tags.get(Tag.Music));
    }

    @Test
    void readByUploaderWithMultipleMedia() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        manager.create(uploaderName, "Video", defaultTags, 10_000_000, defaultCost, 0, defaultResolution, defaultAvailability);
        Map<String, Integer> uploaders = manager.readByUploader();
        assertEquals(2, uploaders.get(uploaderName).intValue());
    }

    @Test
    void readByMediaType() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        manager.create(uploaderName, "Video", defaultTags, 10_000_000, defaultCost, 0, defaultResolution, defaultAvailability);
        List<String> audioMedia = manager.readByMediaType("Audio");
        List<String> videoMedia = manager.readByMediaType("Video");
        assertEquals(1, audioMedia.size());
        assertEquals(1, videoMedia.size());
        assertTrue(audioMedia.get(0).contains("Audio File"));
        assertTrue(videoMedia.get(0).contains("Video File"));
    }

    @Test
    void updateAccessCount() {
        manager.create(uploaderName, "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        String address = manager.read().split("\n")[0].split(" ")[3].replace(",", "");
        manager.updateAccessCount(address);
        assertTrue(manager.read().contains("Access Count: 1"));
    }

    // AudioImplTests

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
    void testAudioImplGetAvailability() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        Duration expectedAvailability = Duration.between(audio.getUploadDate(), LocalDateTime.now());
        Duration actualAvailability = audio.getAvailability();
        long toleranceInMillis = 10;
        assertTrue(Math.abs(expectedAvailability.toMillis() - actualAvailability.toMillis()) <= toleranceInMillis,
                "Availability should match the calculated value within tolerance");
    }

    @Test
    void testAudioImplGetCost() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        assertEquals(defaultCost, audio.getCost(), "Cost should match the provided value");
    }

    @Test
    void testAudioImplSetAccessCount() {
        AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
        audio.setAccessCount(1);
        assertEquals(1, audio.getAccessCount(), "Access count should be updated to 1");
    }

    @Test
    void testAudioImplToString() {
        AudioImpl audio = new AudioImpl(
                defaultSamplingRate,
                "address1",
                defaultTags,
                0,
                5_000_000,
                new UploaderImpl(uploaderName),
                defaultAvailability,
                defaultCost
        );

        String expectedString = String.format(
                "Audio File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                "address1",
                5_000_000 / 1_000_000.0,
                defaultSamplingRate,
                0,
                uploaderName,
                defaultAvailability.toDays(),
                defaultCost,
                defaultTags
        );
        assertEquals(expectedString, audio.toString(), "The toString() method should return the expected string format");
    }

    // AudioVideoImplTests
    @Test
    void testAudioVideoImplGetSamplingRate() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultSamplingRate, av.getSamplingRate(), "Sampling rate should match the default value");
    }

    @Test
    void testAudioVideoImplGetAddress() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals("address1", av.getAddress(), "Address should match the provided value");
    }

    @Test
    void testAudioVideoImplGetTags() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultTags, av.getTags(), "Tags should match the provided value");
    }

    @Test
    void testAudioVideoImplGetAccessCount() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(0, av.getAccessCount(), "Initial access count should be 0");
    }

    @Test
    void testAudioVideoImplGetSize() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(15_000_000, av.getSize(), "Size should match the provided value");
    }

    @Test
    void testAudioVideoImplGetUploader() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(uploaderName, av.getUploader().getName(), "Uploader name should match the provided value");
    }

    @Test
    void testAudioVideoImplGetAvailability() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        Duration expectedAvailability = Duration.between(av.getUploadDate(), LocalDateTime.now());
        Duration actualAvailability = av.getAvailability();
        long toleranceInMillis = 10;
        assertTrue(Math.abs(expectedAvailability.toMillis() - actualAvailability.toMillis()) <= toleranceInMillis,
                "Availability should match the calculated value within tolerance");
    }

    @Test
    void testAudioVideoImplGetCost() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultCost, av.getCost(), "Cost should match the provided value");
    }

    @Test
    void testAudioVideoImplGetResolution() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultResolution, av.getResolution(), "Resolution should match the provided value");
    }

    @Test
    void testAudioVideoImplSetAccessCount() {
        AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        av.setAccessCount(1);
        assertEquals(1, av.getAccessCount(), "Access count should be updated to 1");
    }

    @Test
    void testAudioVideoImplToString() {
        AudioVideoImpl av = new AudioVideoImpl(
                defaultSamplingRate,
                "address1",
                defaultTags,
                0,
                15_000_000,
                new UploaderImpl(uploaderName),
                defaultAvailability,
                defaultCost,
                defaultResolution
        );

        String expectedString = String.format(
                "AudioVideo File [Address: %s, Size: %.2f MB, Sampling Rate: %d, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                "address1",
                15_000_000 / 1_000_000.0,
                defaultSamplingRate,
                defaultResolution,
                0,
                uploaderName,
                defaultAvailability.toDays(),
                defaultCost,
                defaultTags
        );
        assertEquals(expectedString, av.toString(), "The toString() method should return the expected string format");
    }

    //VideoImpl Tests
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

    @Test
    void testVideoImplGetResolution() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        assertEquals(defaultResolution, video.getResolution(), "Resolution should match the provided value");
    }

    @Test
    void testVideoImplSetAccessCount() {
        VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
        video.setAccessCount(1);
        assertEquals(1, video.getAccessCount(), "Access count should be updated to 1");
    }

    @Test
    void testVideoImplToString() {
        VideoImpl video = new VideoImpl(
                "address1",
                defaultTags,
                0,
                10_000_000,
                new UploaderImpl(uploaderName),
                defaultAvailability,
                defaultCost,
                defaultResolution
        );

        String expectedString = String.format(
                "Video File [Address: %s, Size: %.2f MB, Resolution: %d, Access Count: %d, Uploader: %s, Availability: %d Days, Cost: %.2f, Tags: %s]",
                "address1",
                10_000_000 / 1_000_000.0,
                defaultResolution,
                0,
                uploaderName,
                defaultAvailability.toDays(),
                defaultCost,
                defaultTags
        );
        assertEquals(expectedString, video.toString(), "The toString() method should return the expected string format");
    }

}
