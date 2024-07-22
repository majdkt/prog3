package mains;

import domainLogic.Manager;
import contract.Tag;

import java.util.HashSet;
import java.util.Set;

public class Simulation1 {
    private static volatile boolean running = true;

      /*public static void main(String[] args) {
        Manager manager = new Manager();
        String uploaderName = "SimulationUser";

        Thread createThread = new Thread(() -> {
            while (running) {
                // Create media alternately between audio and video
                Set<Tag> tags = new HashSet<>();
                tags.add(Tag.Animal); // Example tag
                if (Math.random() > 0.5) {
                    manager.create(uploaderName, "Audio", tags);
                } else {
                    manager.create(uploaderName, "Video", tags);
                }
                try {
                    Thread.sleep(1000); // Create a new media file every second
                } catch (InterruptedException e) {
                    // Exit the loop if interrupted
                    break;
                }
            }
        });

        Thread deleteThread = new Thread(() -> {
            while (running) {
                if (!manager.read().isEmpty()) {
                    String address = "address_" + ((int)(Math.random() * manager.read().size()) + 1);
                    manager.deleteMedia(address);
                    System.out.println("deleted");
                    manager.read().forEach(System.out::println);
                }
                try {
                    Thread.sleep(2000); // Delete a media file every 2 seconds
                } catch (InterruptedException e) {
                    // Exit the loop if interrupted
                    break;
                }
            }
        });

        createThread.start();
        deleteThread.start();

        try {
            // Let the threads run for some time
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Stop the threads gracefully
        running = false;
        createThread.interrupt();
        deleteThread.interrupt();

        try {
            createThread.join();
            deleteThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Final list of media files:");
        manager.read().forEach(System.out::println);
    } */
}
