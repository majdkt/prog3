import eventSystem.EventDispatcher;
import domainLogic.Manager;
import cliPack.Menu;
import eventSystem.listeners.*;

public class MainCLI {
    public static void main(String[] args) {
        int t = Integer.parseInt(args[0]);
        EventDispatcher eventDispatcher = new EventDispatcher();
        Manager manager = new Manager(t);

        // Add Listeners for specific events
        eventDispatcher.addListener(new CreateMediaEventListener(manager));
        eventDispatcher.addListener(new ReadContentEventListener(manager));
        eventDispatcher.addListener(new ReadByTagEventListener(manager));
        eventDispatcher.addListener(new ReadByUploaderEventListener(manager));
        eventDispatcher.addListener(new ReadByMediaTypeEventListener(manager));
        eventDispatcher.addListener(new UpdateAccessCountEventListener(manager));
        eventDispatcher.addListener(new DeleteEventListener(manager));
        eventDispatcher.addListener(new DeleteUploaderEventListener(manager));
        eventDispatcher.addListener(new SaveStateEventListener(manager));
        eventDispatcher.addListener(new LoadStateJOSEventListener(eventDispatcher,manager));
        eventDispatcher.addListener(new CreateUploaderListener(manager));
        eventDispatcher.addListener(new ReadUsedTagsEventListener(manager));
        eventDispatcher.addListener(new ReadUnusedTagsEventListener(manager));
        eventDispatcher.addListener(new SaveStateJBPEventListener(manager));
        eventDispatcher.addListener(new LoadStateJBPEventListener(eventDispatcher,manager));

        // Start the menu
        Menu menu = new Menu(eventDispatcher);
        menu.run();
    }
}
