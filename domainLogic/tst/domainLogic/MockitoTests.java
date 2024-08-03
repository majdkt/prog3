package domainLogic;

import contract.Observer;
import contract.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MockitoTests {
    private Manager manager;
    private Observer mockObserver1;
    private Observer mockObserver2;
    private List<Observer> spyObservers;

    @BeforeEach
    public void setUp() {
        manager = new Manager(10000);
        mockObserver1 = Mockito.mock(Observer.class);
        mockObserver2 = Mockito.mock(Observer.class);

        // Created spy for the observers list
        spyObservers = spy(new ArrayList<>());
        try {
            java.lang.reflect.Field observersField = Manager.class.getDeclaredField("observers");
            observersField.setAccessible(true);
            observersField.set(manager, spyObservers);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testAddObserver() {
        manager.addObserver(mockObserver1);
        verify(spyObservers).add(mockObserver1);
    }

    @Test
    public void testNotifyObservers() {
        String message = "Test message";
        manager.addObserver(mockObserver1);
        manager.addObserver(mockObserver2);
        manager.notifyObservers(message);
        verify(mockObserver1).update(message);
        verify(mockObserver2).update(message);
    }

    @Test
    public void testCreateUploader() {
        String uploaderName = "Uploader1";
        manager.addObserver(mockObserver1);
        manager.createUploader(uploaderName);
        verify(mockObserver1).update("Uploader created: " + uploaderName);
    }

    @Test
    public void testDeleteUploader() {
        String uploaderName = "Uploader1";
        manager.addObserver(mockObserver1);
        manager.createUploader(uploaderName);
        manager.deleteUploader(uploaderName);
        verify(mockObserver1).update("Uploader deleted: " + uploaderName);
    }
    @Test
    public void testCapacityExceeded() {
        String uploaderName = "Uploader1";
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.News);

        // Create media with a size that would exceed capacity
        manager.create(uploaderName, "Audio", tags, 100001, BigDecimal.valueOf(10), 44100, 0, Duration.ofDays(30));

        // Verify that the correct notification was sent
        verify(mockObserver1).update("Failed to add media: Max capacity exceeded.");
    }

    @Test
    public void testUpdateAccessCount() {
        String uploaderName = "Uploader1";
        String mediaAddress = "1";
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.News);

        // Add media
        manager.create(uploaderName, "Audio", tags, 100, BigDecimal.valueOf(10), 44100, 0, Duration.ofDays(30));

        // Update access count
        manager.updateAccessCount(mediaAddress);

        // Verify that the observer was notified
        verify(mockObserver1).update(contains("Access count updated: " + mediaAddress));
    }

}
