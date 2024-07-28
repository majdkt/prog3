package mains;

import cliPack.Menu;
import eventSystem.EventDispatcher;
import eventSystem.listeners.MediaListener;
import eventSystem.listeners.UploaderListener;
import domainLogic.Manager;

public class MainCLI {
    public static void main(String[] args) {
        // Initialize Manager
        Manager manager = new Manager(1000000000); // Example capacity of 1GB

        // Initialize EventDispatcher
        EventDispatcher eventDispatcher = new EventDispatcher();

        // Add Listeners
        eventDispatcher.addListener(new UploaderListener(manager));
        eventDispatcher.addListener(new MediaListener(manager));

        // Initialize Menu
        Menu menu = new Menu(eventDispatcher);

        // Run Menu
        menu.run();
    }
}
