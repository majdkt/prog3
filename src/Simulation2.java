import contract.ConsoleObserver;
import contract.Tag;
import domainLogic.Manager;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Simulation2 {

    private static final String[] MEDIA_TYPES = {"Audio", "Video", "AudioVideo"};
    private static final Random RANDOM = new Random();
    private static final String[] UPLOADERS = {"Uploader1", "Uploader2", "Uploader3"};

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java MediaSimulation2 <maxCapacity> <numberOfThreads>");
            return;
        }

        long maxCapacity = Long.parseLong(args[0]);
        int numberOfThreads = Integer.parseInt(args[1]);

        Manager manager = new Manager(maxCapacity);
        ConsoleObserver observer = new ConsoleObserver();
        manager.addObserver(observer);

        CountDownLatch insertLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch deleteLatch = new CountDownLatch(numberOfThreads);

        // Create insert threads
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    String uploaderName = UPLOADERS[RANDOM.nextInt(UPLOADERS.length)];
                    String mediaType = MEDIA_TYPES[RANDOM.nextInt(MEDIA_TYPES.length)];
                    Set<Tag> tags = new HashSet<>();
                    long size = RANDOM.nextInt(100) + 1;
                    BigDecimal cost = BigDecimal.valueOf(RANDOM.nextDouble() * 10);
                    int samplingRate = RANDOM.nextInt(44100);
                    int resolution = RANDOM.nextInt(1080);
                    Duration availability = Duration.ofDays(RANDOM.nextInt(30) + 1);

                    System.out.println(Thread.currentThread().getName() + " Attempting to add media...");
                    try {
                        manager.create(uploaderName, mediaType, tags, size, cost, samplingRate, resolution, availability);
                    } catch (IllegalArgumentException e) {
                        System.out.println(Thread.currentThread().getName() + " Failed to Add Media: " + e.getMessage());
                    }
                } finally {
                    insertLatch.countDown();
                }
            }, "Insert-Thread-" + i).start();
        }

        // Wait for all insert threads to complete before starting delete threads
        try {
            insertLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create delete threads
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " Attempting to delete media...");
                    String mediaAddress = null;

                    synchronized (manager) {
                        if (!manager.contentMap.isEmpty()) {
                            mediaAddress = manager.contentMap.keySet().iterator().next();
                        }
                    }

                    if (mediaAddress != null) {
                        synchronized (manager) {
                            manager.deleteMedia(mediaAddress);
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + " No Media to Delete");
                    }
                } finally {
                    deleteLatch.countDown();
                }
            }, "Delete-Thread-" + i).start();
        }

        // Wait for all delete threads to complete
        try {
            deleteLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulation completed.");
    }
}
