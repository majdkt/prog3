package contract;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleObserverTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testUpdate() {
        ConsoleObserver consoleObserver = new ConsoleObserver();
        String testMessage = "Updated";
        consoleObserver.update(testMessage);
        String expectedOutput = "Observer: " + testMessage + System.lineSeparator();
        assertEquals(expectedOutput, outputStreamCaptor.toString(), "Output should match the expected format");
    }
}
