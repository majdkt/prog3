package domainLogic;

import contract.MediaContent;
import contract.Tag;
import contract.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    private Manager manager;

    @BeforeEach
    public void setUp() {
        manager = new Manager(10000); // Set a reasonable capacity for testing
    }

    @Test
    public void testCreateUploader() {
        manager.createUploader("uploader1");
        assertTrue(manager.getAllUploaders().contains("uploader1"), "Uploader should be created.");
    }

    @Test
    public void testDeleteUploader() {
        manager.createUploader("uploader1");
        manager.deleteUploader("uploader1");
        assertFalse(manager.getAllUploaders().contains("uploader1"), "Uploader should be deleted.");
    }

    @Test
    public void testDeleteNonexistentUploader() {
        assertThrows(IllegalArgumentException.class, () -> manager.deleteUploader("nonexistentUploader"), "Should throw exception for nonexistent uploader.");
    }

    @Test
    public void testRead() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        List<String> mediaDetails = manager.read();
        assertEquals(1, mediaDetails.size(), "There should be one media entry.");
        assertTrue(mediaDetails.get(0).contains("Audio File"), "Media details should contain 'Audio File'");
    }

    @Test
    public void testReadByTagWithNoTags() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        Map<Tag, Boolean> tagStatus = manager.readByTag();
        for (Tag tag : Tag.values()) {
            assertFalse(tagStatus.get(tag), "Tag " + tag + " should be false");
        }
    }

    @Test
    public void testReadByTagWithSomeTags() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>(Arrays.asList(Tag.Music, Tag.Review));
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        Map<Tag, Boolean> tagStatus = manager.readByTag();
        assertTrue(tagStatus.get(Tag.Music), "Tag Music should be true");
        assertTrue(tagStatus.get(Tag.Review), "Tag Review should be true");
        assertFalse(tagStatus.get(Tag.Animal), "Tag Animal should be false");
        assertFalse(tagStatus.get(Tag.News), "Tag News should be false");
    }

    @Test
    public void testReadByTagWithAllTags() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>(Arrays.asList(Tag.Music, Tag.Review, Tag.News, Tag.Animal));
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        Map<Tag, Boolean> tagStatus = manager.readByTag();
        for (Tag tag : Tag.values()) {
            assertTrue(tagStatus.get(tag), "Tag " + tag + " should be true");
        }
    }

    @Test
    public void testReadByTagWithMultipleMedia() {
        manager.createUploader("uploader1");
        manager.createUploader("uploader2");

        Set<Tag> tagsForUploader1 = new HashSet<>(Arrays.asList(Tag.Music, Tag.News));
        Set<Tag> tagsForUploader2 = new HashSet<>(Arrays.asList(Tag.Review, Tag.Animal));

        manager.create("uploader1", "Audio", tagsForUploader1, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));
        manager.create("uploader2", "Video", tagsForUploader2, 2000, BigDecimal.valueOf(20), 0, 1080, Duration.ofDays(30));

        Map<Tag, Boolean> tagStatus = manager.readByTag();
        assertTrue(tagStatus.get(Tag.Music), "Tag Music should be true");
        assertTrue(tagStatus.get(Tag.News), "Tag News should be true");
        assertTrue(tagStatus.get(Tag.Review), "Tag Review should be true");
        assertTrue(tagStatus.get(Tag.Animal), "Tag Animal should be true");
    }

    @Test
    public void testReadByUploader() {
        manager.createUploader("uploader1");
        manager.createUploader("uploader2");

        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));
        manager.create("uploader2", "Video", tags, 2000, BigDecimal.valueOf(20), 0, 1080, Duration.ofDays(30));

        Map<String, Integer> uploaderMediaCount = manager.readByUploader();
        assertEquals(1, uploaderMediaCount.get("uploader1"), "Uploader1 should have 1 media item.");
        assertEquals(1, uploaderMediaCount.get("uploader2"), "Uploader2 should have 1 media item.");
    }

    @Test
    public void testReadByMediaType() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));
        manager.create("uploader1", "Video", tags, 2000, BigDecimal.valueOf(20), 0, 1080, Duration.ofDays(30));
        manager.create("uploader1", "AudioVideo", tags, 1500, BigDecimal.valueOf(30), 44100, 1080, Duration.ofDays(30));

        List<String> audioDetails = manager.readByMediaType("Audio");
        List<String> videoDetails = manager.readByMediaType("Video");
        List<String> audioVideoDetails = manager.readByMediaType("AudioVideo");

        assertEquals(1, audioDetails.size(), "There should be one Audio media item.");
        assertEquals(1, videoDetails.size(), "There should be one Video media item.");
        assertEquals(1, audioVideoDetails.size(), "There should be one AudioVideo media item.");
    }

    @Test
    public void testUpdateAccessCount() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        String address = manager.read().get(0).split(" ")[3].split(",")[0];
        manager.updateAccessCount(address);

        MediaContent media = manager.read().stream()
                .filter(detail -> detail.contains(address))
                .map(detail -> manager.contentMap.get(address))
                .findFirst()
                .orElse(null);

        assertNotNull(media, "Media content should not be null");
        assertEquals(1, ((MediaContent) media).getAccessCount(), "Access count should be updated to 1");
    }

    @Test
    public void testDeleteMedia() {
        manager.createUploader("uploader1");
        Set<Tag> tags = new HashSet<>();
        manager.create("uploader1", "Audio", tags, 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        String address = manager.read().get(0).split(" ")[3].split(",")[0];
        manager.deleteMedia(address);

        assertFalse(manager.contentMap.containsKey(address), "Media content should be deleted.");
        assertTrue(manager.availableAddresses.contains(address), "Address should be added to availableAddresses.");
    }

    @Test
    public void testLogout() {
        manager.createUploader("uploader1");
        manager.create("uploader1", "Audio", new HashSet<>(), 1000, BigDecimal.TEN, 44100, 0, Duration.ofDays(30));

        manager.logout();

        assertTrue(manager.getAllUploaders().isEmpty(), "All uploaders should be cleared.");
        assertTrue(manager.contentMap.isEmpty(), "All content should be cleared.");
        assertEquals(0, manager.currentTotalSize, "Total size should be reset.");
        assertEquals(1, manager.addressCounter, "Address counter should be reset.");
        assertTrue(manager.availableAddresses.isEmpty(), "Available addresses should be cleared.");
    }

}
