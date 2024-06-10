import domainLogic.Manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class cliMain {
    public static void main(String[] args) {
        BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
        Manager manager = new Manager();

        Menu menu = new Menu(commandQueue, manager);
        Thread menuThread = new Thread(menu);
        menuThread.start();

        Thread workerThread = new Thread(() -> {
            while (true) {
                try {
                    Command command = commandQueue.take();
                    switch (command.getAction()) {
                        case "create":
                            manager.create();
                            System.out.println("Audio created.");
                            break;
                        case "read":
                            System.out.println("Saved audio files:");
                            manager.read().forEach(System.out::println);
                            break;
                        case "update":
                            manager.update(command.getAddress(), command.getAccessCount());
                            System.out.println(command.getAddress() + " updated with new access count: " + command.getAccessCount());
                            break;
                        case "delete":
                            manager.delete(command.getAddress());
                            //System.out.println(command.getAddress() + " deleted.");
                            break;
                        case "exit":
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Unknown command: " + command.getAction());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Worker thread interrupted");
                    return;
                }
            }
        });

        workerThread.start();

        try {
            menuThread.join();
            workerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted");
        }
    }
}
