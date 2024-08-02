package domainLogic;

import contract.MediaContent;
import contract.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTests {

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
        defaultAvailability = Duration.ofDays(30);
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

    // New test to check if uploader exists
    @Test
    void uploaderExistsCheck() {
        assertTrue(manager.uploaderExists(uploaderName));
        assertFalse(manager.uploaderExists("nonexistentUploader"));
    }

    // New test to ensure the deletion of non-existent uploader throws an exception
    @Test
    void deleteNonExistentUploaderThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.deleteUploader("nonexistentUploader")
        );
    }

    // New test to check if capacity enforcement works correctly
    @Test
    void capacityEnforcement() {
        manager.create(uploaderName, "Audio", defaultTags, MAX_CAPACITY - 1, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        assertEquals(MAX_CAPACITY - 1, manager.getCurrentTotalSize());
        assertThrows(IllegalArgumentException.class, () ->
                manager.create(uploaderName, "Audio", defaultTags, 2, defaultCost, defaultSamplingRate, 0, defaultAvailability)
        );
    }

    // New test to check boundary conditions
    @Test
    void boundaryConditions() {
        manager.create(uploaderName, "Audio", defaultTags, MAX_CAPACITY, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        assertEquals(MAX_CAPACITY, manager.getCurrentTotalSize());
        assertThrows(IllegalArgumentException.class, () ->
                manager.create(uploaderName, "Audio", defaultTags, 1, defaultCost, defaultSamplingRate, 0, defaultAvailability)
        );
    }

    // New test for uploaderExists path
    @Test
    void addUploaderIfNotExists() {
        manager.createUploader("anotherUploader");
        manager.create("anotherUploader", "Audio", defaultTags, 5_000_000, defaultCost, defaultSamplingRate, 0, defaultAvailability);
        assertTrue(manager.uploaderExists("anotherUploader"));
    }



    // New test for AudioVideoImpl path
    @Test
    void formatAudioVideoDetails() {
        manager.create(uploaderName, "AudioVideo", defaultTags, 15_000_000, defaultCost, defaultSamplingRate, defaultResolution, defaultAvailability);
        String details = manager.read();
        assertTrue(details.contains("AudioVideo File"));
    }

    @Test
    void doNotAddExistingUploader() {
        // Arrange
        String existingUploaderName = "existingUploader";
        manager.createUploader(existingUploaderName);  // Add uploader initially

        // Act
        manager.create(existingUploaderName, "Video", defaultTags, 10_000_000, defaultCost, 0, defaultResolution, defaultAvailability);

        // Assert
        // We expect the uploader count to be 1 as the uploader should not be duplicated
        assertEquals(1, manager.readByUploader().size(), "Existing uploader should not be duplicated.");
    }

}
