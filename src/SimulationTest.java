import domainLogic.Manager;

import java.util.concurrent.ThreadLocalRandom;

public class SimulationTest {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Thread createThread = new Thread(() -> {
            while (true) {
                manager.create();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread deleteThread = new Thread(() -> {
            while (true) {
                String address = "address_" + (manager.read().size());
                manager.delete(address);
                manager.read().forEach(System.out::println);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        createThread.start();
        deleteThread.start();

        try {
            createThread.join();
            deleteThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted");
        }

        System.out.println("Final list of audio files:");
        manager.read().forEach(System.out::println);
    }
}
