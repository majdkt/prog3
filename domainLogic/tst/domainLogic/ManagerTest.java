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

class ManagerTest {

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
    void createUploaderAlreadyExists() {
        assertThrows(IllegalArgumentException.class, () -> manager.createUploader(uploaderName));
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

    @Nested
    class AudioImplTests {
        @Test
        void testAudioImplGettersAndSetters() {
            AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
            Duration expectedAvailability = Duration.between(audio.getUploadDate(), LocalDateTime.now());
            Duration actualAvailability = audio.getAvailability();
            long toleranceInMillis = 10;
            assertEquals(defaultSamplingRate, audio.getSamplingRate());
            assertEquals("address1", audio.getAddress());
            assertEquals(defaultTags, audio.getTags());
            assertEquals(0, audio.getAccessCount());
            assertEquals(5_000_000, audio.getSize());
            assertEquals(uploaderName, audio.getUploader().getName());
            assertTrue(Math.abs(expectedAvailability.toMillis() - actualAvailability.toMillis()) <= toleranceInMillis);
            assertEquals(defaultCost, audio.getCost());

            audio.setAccessCount(1);
            assertEquals(1, audio.getAccessCount());
        }
    }

    @Nested
    class VideoImplTests {
        @Test
        void testVideoImplGettersAndSetters() {
            VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
            assertEquals("address1", video.getAddress());
            assertEquals(defaultTags, video.getTags());
            assertEquals(0, video.getAccessCount());
            assertEquals(10_000_000, video.getSize());
            assertEquals(uploaderName, video.getUploader().getName());
            assertEquals(defaultCost, video.getCost());
            assertEquals(defaultResolution, video.getResolution());

            video.setAccessCount(1);
            assertEquals(1, video.getAccessCount());
        }
    }

    @Nested
    class AudioVideoImplTests {
        @Test
        void testAudioVideoImplGettersAndSetters() {
            AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
            Duration expectedAvailability = Duration.between(av.getUploadDate(), LocalDateTime.now());
            Duration actualAvailability = av.getAvailability();
            long toleranceInMillis = 10;
            assertEquals(defaultSamplingRate, av.getSamplingRate());
            assertEquals("address1", av.getAddress());
            assertEquals(defaultTags, av.getTags());
            assertEquals(0, av.getAccessCount());
            assertEquals(15_000_000, av.getSize());
            assertEquals(uploaderName, av.getUploader().getName());
            assertTrue(Math.abs(expectedAvailability.toMillis() - actualAvailability.toMillis()) <= toleranceInMillis);
            assertEquals(defaultCost, av.getCost());
            assertEquals(defaultResolution, av.getResolution());

            av.setAccessCount(1);
            assertEquals(1, av.getAccessCount());
        }

        @Test
        void testAudioVideoImplToString() {
            AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15_000_000, new UploaderImpl(uploaderName), null, defaultCost, defaultResolution);
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
            System.out.println(expectedString);
            assertEquals(expectedString, av.toString());
        }
    }
}
