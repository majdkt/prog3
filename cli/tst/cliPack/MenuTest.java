package cliPack;

import eventSystem.EventDispatcher;
import eventSystem.events.CreateUploaderEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MenuTest {

    @Test
    public void testCreateUploaderViaCLI() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        Menu menu = new Menu(eventDispatcher);

        // Use reflection to set the scanner
        Field scannerField = Menu.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        Scanner scanner = new Scanner("uploaderName\n:q");
        scannerField.set(menu, scanner);

        // Act
        menu.run();

        // Assert
        ArgumentCaptor<CreateUploaderEvent> captor = ArgumentCaptor.forClass(CreateUploaderEvent.class);
        verify(eventDispatcher).dispatch(captor.capture());
        CreateUploaderEvent event = captor.getValue();
        assertEquals("uploaderName", event.getUploaderName());
    }
}
