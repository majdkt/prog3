package cliPack;

import eventSystem.EventDispatcher;
import eventSystem.events.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuTest {

    private EventDispatcher eventDispatcher;
    private Menu menu;

    @BeforeEach
    void setUp() {
        eventDispatcher = Mockito.mock(EventDispatcher.class);
        menu = new Menu(eventDispatcher);
    }

    private void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void testHandleCreateUploader() {
        String input = ":c\nNewUploader\n"; // Simulates input to create an uploader
        setInput(input);

        // Run the Menu
        menu.run();

        // Verify that the correct event was dispatched
        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof CreateUploaderEvent &&
                        "NewUploader".equals(((CreateUploaderEvent) event).getUploaderName())
        ));
    }

    @Test
    void testHandleCreateMedia() {
        // Simulate input to create media
        String input = ":c\nAudio NewUploader , 1024 9.99\ndone\n";
        setInput(input);

        // Run the Menu
        menu.run();

        // Verify that the correct events were dispatched
        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof CreateUploaderEvent &&
                        "NewUploader".equals(((CreateUploaderEvent) event).getUploaderName())
        ));

        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof CreateMediaEvent &&
                        "NewUploader".equals(((CreateMediaEvent) event).getUploaderName()) &&
                        "Audio".equals(((CreateMediaEvent) event).getMediaType()) &&
                        ((CreateMediaEvent) event).getTags().isEmpty() &&
                        1024 == ((CreateMediaEvent) event).getSize() &&
                        new BigDecimal("9.99").equals(((CreateMediaEvent) event).getCost())
        ));
    }

    @Test
    void testHandleDeleteUploader() {
        // Simulate input to delete an uploader
        String input = ":d\nUploaderToDelete\n";
        setInput(input);

        // Run the Menu
        menu.run();

        // Verify that the correct event was dispatched
        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof DeleteUploaderEvent &&
                        "UploaderToDelete".equals(((DeleteUploaderEvent) event).getUploaderName())
        ));
    }

    @Test
    void testHandleUpdate() {
        // Simulate input to update media access count
        String input = ":u\nsomeMediaAddress\n";
        setInput(input);

        // Run the Menu
        menu.run();

        // Verify that the correct event was dispatched
        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof UpdateAccessCountEvent &&
                        "someMediaAddress".equals(((UpdateAccessCountEvent) event).getAddress())
        ));
    }

    @Test
    void testHandleRead() {
        // Simulate input to read content by media type
        String input = ":r\ncontent Audio\n";
        setInput(input);

        // Run the Menu
        menu.run();

        // Verify that the correct event was dispatched
        verify(eventDispatcher).dispatch(argThat(event ->
                event instanceof ReadByMediaTypeEvent &&
                        "Audio".equals(((ReadByMediaTypeEvent) event).getMediaType())
        ));
    }
}
