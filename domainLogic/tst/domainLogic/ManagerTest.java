package domainLogic;

import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    void create() {
        // Create a Manager instance
        Manager manager = new Manager("");

        // Create sample Uploader
        Uploader uploader = new UploaderImpl("Majd");

        // Create sample Uploadable
        Duration availability = Duration.ofHours(1);
        BigDecimal cost = BigDecimal.valueOf(10);
        Uploadable uploadable = new UploadableImpl(uploader, availability, cost);

        // Create sample MediaContent
        int samplingRate = 44100;
        String address = "audio_address";
        long size = 100;
        MediaContent mediaContent = new AudioImpl(samplingRate, address, size, uploader);

        // Call the create method
        manager.create(uploader.getName(),mediaContent, uploadable);

        // Check if the mediaList contains the newly created mediaContent
        assertTrue(manager.read().contains(mediaContent));

        // Check if the taskAssignments map contains the newly created mediaContent and uploadable pair
        assertTrue(manager.getTaskAssignments().containsKey(mediaContent));
        assertEquals(uploadable, manager.getTaskAssignments().get(mediaContent));

        // Additional assertions to check if saved data is correct
        assertEquals(samplingRate, ((AudioImpl) mediaContent).getSamplingRate());
        assertEquals(address, mediaContent.getAddress());
        assertEquals(size, mediaContent.getSize());
        assertEquals(uploader, ((AudioImpl) mediaContent).getUploader());
        assertEquals(availability, uploadable.getAvailability());
        assertEquals(cost, uploadable.getCost());
    }


    @Test
    void read() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}