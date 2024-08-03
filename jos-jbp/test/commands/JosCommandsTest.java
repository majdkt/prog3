package commands;

import domainLogic.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class JosCommandsTest {

    private JosCommands josCommands;
    private Manager manager;

    @BeforeEach
    void setUp() {
        josCommands = new JosCommands();
        manager = new Manager(10000);
    }

    @AfterEach
    void tearDown() {
        File file = new File("Saved");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveAndLoadState() throws IOException, ClassNotFoundException {
        josCommands.saveState(manager);
        Manager loadedManager = josCommands.loadState();
        assertNotNull(loadedManager, "Loaded manager should not be null");
        assertEquals(manager.getCurrentTotalSize(), loadedManager.getCurrentTotalSize(), "Current total size should match");
    }

    @Test
    void testLoadStateWithoutSaving() throws IOException, ClassNotFoundException {
        Manager loadedManager = josCommands.loadState();
        assertNull(loadedManager, "Loaded manager should be null if no file exists");
    }
}
