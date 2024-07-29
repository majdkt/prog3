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
    private static final long MAX_CAPACITY = 100000000; // 100 MB
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
        defaultAvailability = Duration.ofDays(1); // Default availability set to 1 day for testing
        uploaderName = "testUploader";
        manager.createUploader(uploaderName);
    }

    @Test
    void create_audio_successfully() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        assertTrue(manager.getCurrentTotalSize() > 0);
    }

    @Test
    void create_video_successfully() {
        manager.create(uploaderName, "Video", defaultTags, 10000000, defaultCost, 0, defaultResolution, defaultAvailability);
        assertTrue(manager.getCurrentTotalSize() > 0);
    }

    @Test
    void create_audio_video_successfully() {
        manager.create(uploaderName, "AudioVideo", defaultTags, 15000000, defaultCost, defaultSamplingRate, defaultResolution, defaultAvailability);
        assertTrue(manager.getCurrentTotalSize() > 0);
    }

    @Test
    void create_with_invalid_media_type_throws_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.create(uploaderName, "InvalidType", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability)
        );
    }

    @Test
    void create_exceeding_capacity_throws_exception() {
        while (manager.getCurrentTotalSize() < MAX_CAPACITY) {
            manager.create(uploaderName, "Audio", defaultTags, 1000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        }
        assertThrows(IllegalArgumentException.class, () ->
                manager.create(uploaderName, "Audio", defaultTags, 1000000, defaultCost, defaultSamplingRate, 0, defaultAvailability)
        );
    }

    @Test
    void delete_media_successfully() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        String address = manager.read().split("\n")[0].split(" ")[3].replace(",", "");
        manager.deleteMedia(address);
        assertFalse(manager.read().contains(address));
    }

    @Test
    void delete_media_not_found() {
        // No assertion needed, just ensuring the method does not throw an exception
        manager.deleteMedia("nonexistentAddress");
    }

    @Test
    void create_uploader_successfully() {
        manager.createUploader("newUploader");
        assertTrue(manager.uploaderExists("newUploader"));
    }

    @Test
    void create_uploader_already_exists() {
        assertThrows(IllegalArgumentException.class, () -> manager.createUploader(uploaderName));
    }

    @Test
    void delete_uploader_successfully() {
        manager.deleteUploader(uploaderName);
        assertFalse(manager.uploaderExists(uploaderName));
    }

    @Test
    void delete_uploader_with_media_removes_media() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        manager.deleteUploader(uploaderName);
        assertEquals(0, manager.getCurrentTotalSize());
    }

    @Test
    void read_media_details() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        String details = manager.read();
        assertTrue(details.contains("Audio File"));
    }

    @Test
    void read_by_tag_with_existing_tag() {
        manager.create(uploaderName, "Video", defaultTags, 10000000, defaultCost, 0, defaultResolution, defaultAvailability);
        Map<Tag, Boolean> tags = manager.readByTag();
        assertTrue(tags.get(Tag.News));
    }

    @Test
    void read_by_tag_with_non_existing_tag() {
        manager.create(uploaderName, "Video", defaultTags, 10000000, defaultCost, 0, defaultResolution, defaultAvailability);
        Map<Tag, Boolean> tags = manager.readByTag();
        assertFalse(tags.get(Tag.Music));
    }

    @Test
    void read_by_uploader_with_multiple_media() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        manager.create(uploaderName, "Video", defaultTags, 10000000, defaultCost, 0, defaultResolution, defaultAvailability);
        Map<String, Integer> uploaders = manager.readByUploader();
        assertEquals(2, uploaders.get(uploaderName).intValue());
    }

    @Test
    void read_by_media_type() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        manager.create(uploaderName, "Video", defaultTags, 10000000, defaultCost, 0, defaultResolution, defaultAvailability);
        List<String> audioMedia = manager.readByMediaType("Audio");
        List<String> videoMedia = manager.readByMediaType("Video");
        assertEquals(1, audioMedia.size());
        assertEquals(1, videoMedia.size());
        assertTrue(audioMedia.get(0).contains("Audio File"));
        assertTrue(videoMedia.get(0).contains("Video File"));
    }

    @Test
    void update_access_count() {
        manager.create(uploaderName, "Audio", defaultTags, 5000000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        String address = manager.read().split("\n")[0].split(" ")[3].replace(",", "");
        manager.updateAccessCount(address);
        assertTrue(manager.read().contains("Access Count: 1"));
    }

    // ... (Nested classes for AudioImplTests, VideoImplTests, AudioVideoImplTests with similar test structures)
    @Nested
    class AudioImplTests {
        @Test
        void testAudioImplGettersAndSetters() {
            AudioImpl audio = new AudioImpl(defaultSamplingRate, "address1", defaultTags, 0, 5000000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost);
            Duration expectedAvailability = Duration.between(audio.getUploadDate(), LocalDateTime.now());
            Duration actualAvailability = audio.getAvailability();
            long toleranceInMillis = 10;
            assertEquals(defaultSamplingRate, audio.getSamplingRate());
            assertEquals("address1", audio.getAddress());
            assertEquals(defaultTags, audio.getTags());
            assertEquals(0, audio.getAccessCount());
            assertEquals(5000000, audio.getSize());
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
            VideoImpl video = new VideoImpl("address1", defaultTags, 0, 10000000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
            assertEquals("address1", video.getAddress());
            assertEquals(defaultTags, video.getTags());
            assertEquals(0, video.getAccessCount());
            assertEquals(10000000, video.getSize());
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
            AudioVideoImpl av = new AudioVideoImpl(defaultSamplingRate, "address1", defaultTags, 0, 15000000, new UploaderImpl(uploaderName), defaultAvailability, defaultCost, defaultResolution);
            Duration expectedAvailability = Duration.between(av.getUploadDate(), LocalDateTime.now());
            Duration actualAvailability = av.getAvailability();
            long toleranceInMillis = 10;
            assertEquals(defaultSamplingRate, av.getSamplingRate());
            assertEquals("address1", av.getAddress());
            assertEquals(defaultTags, av.getTags());
            assertEquals(0, av.getAccessCount());
            assertEquals(15000000, av.getSize());
            assertEquals(uploaderName, av.getUploader().getName());
            assertTrue(Math.abs(expectedAvailability.toMillis() - actualAvailability.toMillis()) <= toleranceInMillis);
            assertEquals(defaultCost, av.getCost());
            assertEquals(defaultResolution, av.getResolution());

            av.setAccessCount(1);
            assertEquals(1, av.getAccessCount());
        }
    }
}
