package domainLogic;

import contract.MediaContent;
import contract.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    private Manager manager;
    private final String uploaderName = "TestUploader";
    private final Set<Tag> tags = new HashSet<>();

    @BeforeEach
    public void setUp() {
        manager = new Manager();
    }

    @Test
    public void testCreateAudio() {
        manager.create(uploaderName, "Audio", tags);

        // Verify observable outcomes
        List<String> mediaDetails = manager.read();
        assertFalse(mediaDetails.isEmpty(), "Media details should not be empty.");
        assertTrue(mediaDetails.stream().anyMatch(detail -> detail.contains("Audio File")), "Media details should contain 'Audio File'.");
    }

    @Test
    public void testCreateVideo() {
        manager.create(uploaderName, "Video", tags);

        // Verify observable outcomes
        List<String> mediaDetails = manager.read();
        assertFalse(mediaDetails.isEmpty(), "Media details should not be empty.");
        assertTrue(mediaDetails.stream().anyMatch(detail -> detail.contains("Video File")), "Media details should contain 'Video File'.");
    }

    @Test
    public void testCreateAudioVideo() {
        manager.create(uploaderName, "AudioVideo", tags);

        // Verify observable outcomes
        List<String> mediaDetails = manager.read();
        assertFalse(mediaDetails.isEmpty(), "Media details should not be empty.");
        assertTrue(mediaDetails.stream().anyMatch(detail -> detail.contains("AudioVideo File")), "Media details should contain 'AudioVideo File'.");
    }

    @Test
    public void testCreateInvalidMediaType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.create(uploaderName, "InvalidType", tags);
        });

        assertEquals("Invalid media type.", exception.getMessage(), "Exception message should be 'Invalid media type.'");
    }

    @Test
    public void testUpdateAccessCount() {
        manager.create("Uploader3", "Audio", new HashSet<>());
        String address = getFirstMediaAddress();
        manager.updateAccessCount(address);

        MediaContent media = manager.getContentMap().get(address);
        assertNotNull(media, "Media should exist.");
        assertEquals(1, media.getAccessCount(), "Access count should be updated.");
    }


    @Test
    public void testDeleteMedia() {
        manager.create(uploaderName, "Audio", tags);
        String address = getFirstMediaAddress(); // Get the address of the created media
        manager.deleteMedia(address); // Delete the media

        // Verify observable outcomes
        List<String> mediaDetails = manager.read();
        boolean isDeleted = mediaDetails.stream().noneMatch(detail -> detail.contains("Address: " + address));
        assertTrue(isDeleted, "Deleted media should not be in the list.");
    }
    @Test
    public void testLogout() {
        manager.create(uploaderName, "Audio", tags);
        manager.logout();

        // Verify observable outcomes
        List<String> mediaDetails = manager.read();
        assertTrue(mediaDetails.isEmpty(), "Media details should be empty after logout.");
    }

    // Helper Methods
    private String getFirstMediaAddress() {
        List<String> mediaDetails = manager.read();
        assertFalse(mediaDetails.isEmpty(), "No media found.");

        // Extract the address from the media details string
        String firstDetail = mediaDetails.get(0);
        String addressPrefix = "Address: ";
        int startIndex = firstDetail.indexOf(addressPrefix) + addressPrefix.length();
        int endIndex = firstDetail.indexOf(",", startIndex);
        return firstDetail.substring(startIndex, endIndex).trim();
    }

    @Test
    public void testCreateAndUpdateAccessCount() {
        // Create 5 media files

        for (int i = 1; i <= 5; i++) {
            manager.create("Uploader" + i, "Audio", tags);
        }

        // Retrieve the addresses of the created media files
        Map<String, MediaContent> contentMap = manager.getContentMap();
        assertEquals(5, contentMap.size(), "There should be 5 media files created.");

        // Get the address of the third media file
        String thirdMediaAddress = getNthMediaAddress(3);

        // Update access count for the third media file
        manager.updateAccessCount(thirdMediaAddress);

        // Retrieve the updated media content
        MediaContent thirdMedia = manager.getContentMap().get(thirdMediaAddress);
        assertNotNull(thirdMedia, "The third media file should exist.");
        assertEquals(1, thirdMedia.getAccessCount(), "Access count for the third media file should be 1.");
    }

    private String getNthMediaAddress(int n) {
        // Retrieve the addresses from the media details
        List<String> mediaDetails = manager.read();
        assertTrue(mediaDetails.size() >= n, "Not enough media files created.");

        String nthDetail = mediaDetails.get(n - 1); // Get the nth media detail
        String addressPrefix = "Address: ";
        int startIndex = nthDetail.indexOf(addressPrefix) + addressPrefix.length();
        int endIndex = nthDetail.indexOf(",", startIndex);
        return nthDetail.substring(startIndex, endIndex).trim();
    }

}
