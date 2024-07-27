package domainLogic;

import contract.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    private static final long MAX_CAPACITY = 1_000_000; // 1 MB for testing
    private Manager manager;

    @BeforeEach
    public void setUp() {
        manager = new Manager(MAX_CAPACITY);
    }

    @Test
    public void testCreateUploader() {
        manager.createUploader("uploader1");
        assertTrue(manager.getAllUploaders().contains("uploader1"));
    }

    @Test
    public void testCreateDuplicateUploader() {
        manager.createUploader("uploader1");
        assertThrows(IllegalArgumentException.class, () -> manager.createUploader("uploader1"));
    }

    @Test
    public void testDeleteUploader() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        manager.deleteUploader("uploader1");
        assertFalse(manager.getAllUploaders().contains("uploader1"));
        assertTrue(manager.read().isEmpty());
    }

    @Test
    public void testDeleteNonexistentUploader() {
        assertThrows(IllegalArgumentException.class, () -> manager.deleteUploader("uploader1"));
    }

    @Test
    public void testGetAllUploaders() {
        manager.createUploader("uploader1");
        manager.createUploader("uploader2");
        List<String> uploaders = manager.getAllUploaders();
        assertEquals(2, uploaders.size());
        assertTrue(uploaders.contains("uploader1"));
        assertTrue(uploaders.contains("uploader2"));
    }

    @Test
    public void testCreateAudioMedia() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        assertEquals(1, manager.read().size());
    }

    @Test
    public void testCreateVideoMedia() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "Video", tags, 2000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        assertEquals(1, manager.read().size());
    }

    @Test
    public void testCreateAudioVideoMedia() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "AudioVideo", tags, 3000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        assertEquals(1, manager.read().size());
    }

    @Test
    public void testCreateMediaExceedsCapacity() {
        manager.createUploader("uploader1");
        assertThrows(IllegalArgumentException.class, () -> manager.create("uploader1", "Audio", new HashSet<>(), MAX_CAPACITY + 1, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30)));
    }

    @Test
    public void testCreateMediaInvalidType() {
        manager.createUploader("uploader1");
        assertThrows(IllegalArgumentException.class, () -> manager.create("uploader1", "InvalidType", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30)));
    }

    @Test
    public void testReadByTag() {
        // Add media content with various tags
       // manager.create("uploader1", "Audio", Set.of(Tag.Music, Tag.Review), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
       // manager.create("uploader2", "Video", Set.of(Tag.News), 2000, BigDecimal.valueOf(20), 44100, 1080, Duration.ofDays(30));

        // Verify the status of tags
        Map<Tag, Boolean> tagStatus = manager.readByTag();
        assertTrue(tagStatus.get(Tag.Music));
        assertTrue(tagStatus.get(Tag.Review));
        assertTrue(tagStatus.get(Tag.News));
        assertFalse(tagStatus.get(Tag.Animal)); // Assuming Tag.Animal was not used
    }

    @Test
    public void testReadByUploader() {
        // Create uploaders
        manager.createUploader("uploader1");
        manager.createUploader("uploader2");

        // Upload media for each uploader
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        manager.create("uploader1", "Video", new HashSet<>(), 2000, BigDecimal.valueOf(20), 44100, 1080, Duration.ofDays(30));
        manager.create("uploader2", "Video", new HashSet<>(), 2000, BigDecimal.valueOf(20), 44100, 1080, Duration.ofDays(30));

        // Read media counts by uploader
        Map<String, Integer> mediaCounts = manager.readByUploader();

        // Verify media counts
        assertEquals(2, mediaCounts.size(), "There should be 2 uploaders in the map");
        assertEquals(2, mediaCounts.get("uploader1"), "uploader1 should have 2 media items");
        assertEquals(1, mediaCounts.get("uploader2"), "uploader2 should have 1 media item");
    }

    @Test
    public void testReadByMediaType() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        manager.create("uploader1", "Video", new HashSet<>(), 2000, BigDecimal.valueOf(20), 44100, 1080, Duration.ofDays(30));
        manager.create("uploader1", "AudioVideo", new HashSet<>(), 3000, BigDecimal.valueOf(30), 44100, 1080, Duration.ofDays(30));
        assertEquals(1, manager.readByMediaType("Audio").size());
        assertEquals(1, manager.readByMediaType("Video").size());
        assertEquals(1, manager.readByMediaType("AudioVideo").size());
    }

    @Test
    public void testUpdateAccessCount() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        String address = manager.read().get(0).split("\\[")[1].split(",")[0].split(":")[1].trim();
        manager.updateAccessCount(address);
        assertTrue(manager.read().get(0).contains("Access Count: 1"));
    }

    @Test
    public void testDeleteMedia() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        String address = manager.read().get(0).split("\\[")[1].split(",")[0].split(":")[1].trim();
        manager.deleteMedia(address);
        assertTrue(manager.read().isEmpty());
    }

    @Test
    public void testLogout() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 1080, Duration.ofDays(30));
        manager.logout();
        assertTrue(manager.getAllUploaders().isEmpty());
        assertTrue(manager.read().isEmpty());
    }

    // Tests for AudioImpl, VideoImpl, and AudioVideoImpl
    @Test
    public void testAudioImpl() {
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.News);
        AudioImpl audio = new AudioImpl(44100, "address1", tags, 0, 1000, new UploaderImpl("uploader1"), Duration.ofDays(30), BigDecimal.TEN);
        assertEquals(44100, audio.getSamplingRate());
        assertEquals("address1", audio.getAddress());
        assertEquals(tags, audio.getTags());
        assertEquals(0, audio.getAccessCount());
        assertEquals(1000, audio.getSize());
        assertEquals("uploader1", audio.getUploader().getName());
        assertEquals(Duration.ofDays(30), audio.getAvailability());
        assertEquals(BigDecimal.TEN, audio.getCost());

        audio.setAccessCount(5);
        assertEquals(5, audio.getAccessCount());
    }

    @Test
    public void testVideoImpl() {
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.Animal);
        VideoImpl video = new VideoImpl("address1", tags, 0, 2000, new UploaderImpl("uploader1"), Duration.ofDays(30), BigDecimal.TEN, 1080);
        assertEquals(1080, video.getResolution());
        assertEquals("address1", video.getAddress());
        assertEquals(tags, video.getTags());
        assertEquals(0, video.getAccessCount());
        assertEquals(2000, video.getSize());
        assertEquals("uploader1", video.getUploader().getName());
        assertEquals(Duration.ofDays(30), video.getAvailability());
        assertEquals(BigDecimal.TEN, video.getCost());

        video.setAccessCount(5);
        assertEquals(5, video.getAccessCount());
    }

    @Test
    public void testAudioVideoImpl() {
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.Music);
        AudioVideoImpl audioVideo = new AudioVideoImpl(44100, "address1", tags, 0, 3000, new UploaderImpl("uploader1"), Duration.ofDays(30), BigDecimal.TEN, 1080);
        assertEquals(44100, audioVideo.getSamplingRate());
        assertEquals(1080, audioVideo.getResolution());
        assertEquals("address1", audioVideo.getAddress());
        assertEquals(tags, audioVideo.getTags());
        assertEquals(0, audioVideo.getAccessCount());
        assertEquals(3000, audioVideo.getSize());
        assertEquals("uploader1", audioVideo.getUploader().getName());
        assertEquals(Duration.ofDays(30), audioVideo.getAvailability());
        assertEquals(BigDecimal.TEN, audioVideo.getCost());

        audioVideo.setAccessCount(5);
        assertEquals(5, audioVideo.getAccessCount());
    }
}
