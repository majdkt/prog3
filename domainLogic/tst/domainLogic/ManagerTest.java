package domainLogic;

import contract.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static contract.Tag.Music;
import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    void create() {
        Manager manager = new Manager();
        Uploader uploader = new UploaderImpl("Name");
        Uploadable uploadable = new UploadableImpl(uploader,uploader.getName());
        MediaContent m = new MediaContentImpl("Berlin", Music,21,3);
        manager.create(m,uploadable);
        assertEquals("Name",uploader.getName());
        assertEquals(Music,m.getTags());
    }

    @Test
    void read() {
        Manager manager = new Manager();
        manager.read();

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}