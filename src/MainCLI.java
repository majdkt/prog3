import eventSystem.EventDispatcher;
import eventSystem.listeners.MediaListener;
import domainLogic.Manager;
import cliPack.Menu;
import eventSystem.listeners.UploaderListener;

public class MainCLI {
    public static void main(String[] args) {
        EventDispatcher eventDispatcher = new EventDispatcher();
        Manager manager = new Manager(1000);

        // Add Listeners
        eventDispatcher.addListener(new UploaderListener(manager));
        eventDispatcher.addListener(new MediaListener(manager));

        // Start the menu
        Menu menu = new Menu(eventDispatcher);
        menu.run();
    }
}
