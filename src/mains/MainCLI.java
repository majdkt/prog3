package mains;

import cliPack.Menu;
import domainLogic.Manager;
import eventSystem.EventDispatcher;

public class MainCLI {
    public static void main(String[] args) {
        // Initialize the event dispatcher
        EventDispatcher eventDispatcher = new EventDispatcher();

        // Initialize the manager with a maximum capacity (e.g., 10 GB)
        long maxTotalCapacity = 10L * 1024 * 1024 * 1024; // 10 GB in bytes
        Manager manager = new Manager(maxTotalCapacity);
        // Initialize and run the menu
        Menu menu = new Menu(eventDispatcher);
        menu.run();
    }
}
