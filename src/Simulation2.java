import domainLogic.Manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation2 {
    public static void main(String[] args) {
        BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
        Manager manager = new Manager();

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
                            Thread workerThread1 = new Thread(() -> {
                                manager.delete(command.getAddress());
                                System.out.println(command.getAddress() + " deleted.");
                            });
                            workerThread1.start();
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

        Thread simulationThread = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    commandQueue.put(new Command("create"));
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500)); // Random delay
                }
                // Randomly update some files
                for (int i = 1; i <= 3; i++) {
                    String address = "address_" + i;
                    long newAccessCount = ThreadLocalRandom.current().nextLong(1, 100);
                    commandQueue.put(new Command("update", address, newAccessCount));
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500)); // Random delay
                }

                commandQueue.put(new Command("read"));
                commandQueue.put(new Command("delete", "address_1")); // Delete one file
                commandQueue.put(new Command("read"));
                commandQueue.put(new Command("exit"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulation2 interrupted");
            }
        });

        simulationThread.start();

        try {
            simulationThread.join();
            workerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted");
        }
    }
}
