import eventSystem.EventDispatcher;
import domainLogic.Manager;
import cliPack.Menu;
import eventSystem.listeners.*;
import serverClient.ServerLogic;

//Disabled functionalities: ReadByTag, Delete uploader
public class AlternativCLI {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java MainCLI <protocol> <capacity>");
            System.exit(1);
        }

        String protocol = args[0];
        int capacity;

        try {
            capacity = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid capacity format. It should be an integer.");
            System.exit(1);
            return;
        }

        EventDispatcher eventDispatcher = new EventDispatcher();
        Manager manager = new Manager(capacity);

        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            ServerLogic server = new ServerLogic(12345, protocol, manager);
            server.start();
        });
        serverThread.start();

        // Add Listeners for specific events
        eventDispatcher.addListener(new CreateMediaEventListener(manager));
        eventDispatcher.addListener(new ReadContentEventListener(manager));
        eventDispatcher.addListener(new ReadByUploaderEventListener(manager));
        eventDispatcher.addListener(new ReadByMediaTypeEventListener(manager));
        eventDispatcher.addListener(new UpdateAccessCountEventListener(manager));
        eventDispatcher.addListener(new AlternativCLIEventListener(manager));
        eventDispatcher.addListener(new SaveStateEventListener(manager));
        eventDispatcher.addListener(new LoadStateJOSEventListener(eventDispatcher, manager));
        eventDispatcher.addListener(new CreateUploaderListener(manager));
        eventDispatcher.addListener(new DeleteEventListener(manager));
        //eventDispatcher.addListener(new DeleteUploaderEventListener(manager));
        //eventDispatcher.addListener(new ReadByTagEventListener(manager));
        //eventDispatcher.addListener(new ReadUsedTagsEventListener(manager));
        //eventDispatcher.addListener(new ReadUnusedTagsEventListener(manager));
        eventDispatcher.addListener(new SaveStateJBPEventListener(manager));
        eventDispatcher.addListener(new LoadStateJBPEventListener(eventDispatcher, manager));

        Menu menu = new Menu(eventDispatcher);
        menu.run();
    }
}

