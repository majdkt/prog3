package cliPack;

import static org.mockito.Mockito.*;
import eventSystem.EventDispatcher;
import eventSystem.events.CheckUploaderExistenceEvent;
import eventSystem.events.CreateUploaderEvent;
import eventSystem.events.ReadByUploaderEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

class MenuTest {

    @Mock
    private EventDispatcher eventDispatcher;

    @InjectMocks
    private Menu menu;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStreamCaptor)); // Redirect System.out
    }

    @Test
    void testCreateUploader() {
        Scanner scanner = new Scanner("JohnDoe\n:done");
        menu = new Menu(eventDispatcher);
        menu.scanner = scanner;
        menu.handleCreate();

        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof CheckUploaderExistenceEvent &&
                        "JohnDoe".equals(((CheckUploaderExistenceEvent) event).getUploaderName())
        ));

        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof CreateUploaderEvent &&
                        "JohnDoe".equals(((CreateUploaderEvent) event).getUploaderName())
        ));
    }

    @Test
    void testReadByUploader() {
        Scanner scanner = new Scanner("uploader\n:quit");
        menu = new Menu(eventDispatcher);
        menu.scanner = scanner;
        menu.handleRead();

        verify(eventDispatcher).dispatch(any(ReadByUploaderEvent.class));
    }
}
