import eventSystem.EventDispatcher;
import eventSystem.listeners.MediaListener;
import domainLogic.Manager;
import cliPack.Menu;
import eventSystem.listeners.UploaderListener;

import javax.swing.event.MenuListener;

public class MainCLI {
    public static void main(String[] args) {
        int t = Integer.parseInt(args[0]);
        EventDispatcher eventDispatcher = new EventDispatcher();
        Manager manager = new Manager(t);

        // Add Listeners
        eventDispatcher.addListener(new UploaderListener(manager));
        eventDispatcher.addListener(new MediaListener(manager));

        // Start the menu
        Menu menu = new Menu(eventDispatcher);
        menu.run();
    }
}
