import contract.Tag;
import domainLogic.Manager;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

public class Simulation3 {

    private static final String[] MEDIA_TYPES = {"Audio", "Video", "AudioVideo"};
    private static final Random RANDOM = new Random();
    private static final String[] UPLOADERS = {"Uploader1", "Uploader2", "Uploader3"};

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java MediaSimulation3 <maxCapacity> <numberOfThreads> <intervalInMillis>");
            return;
        }

        long maxCapacity = Long.parseLong(args[0]);
        int numberOfThreads = Integer.parseInt(args[1]);
        long interval = Long.parseLong(args[2]);

        Manager manager = new Manager(maxCapacity);

        // Create thread pools
        ExecutorService insertExecutor = Executors.newFixedThreadPool(numberOfThreads);
        ExecutorService deleteExecutor = Executors.newFixedThreadPool(numberOfThreads);
        ScheduledExecutorService statusExecutor = Executors.newSingleThreadScheduledExecutor();

        // Schedule periodic status reporting
        statusExecutor.scheduleAtFixedRate(() -> {
            synchronized (manager) {
                System.out.println("Current state:");
                System.out.println(manager.read());
            }
        }, 0, interval, TimeUnit.MILLISECONDS);

        // Create insert threads
        for (int i = 0; i < numberOfThreads; i++) {
            insertExecutor.submit(() -> {
                try {
                    while (true) {
                        String uploaderName = UPLOADERS[RANDOM.nextInt(UPLOADERS.length)];
                        String mediaType = MEDIA_TYPES[RANDOM.nextInt(MEDIA_TYPES.length)];
                        Set<Tag> tags = new HashSet<>();
                        long size = RANDOM.nextInt(100) + 1;
                        BigDecimal cost = BigDecimal.valueOf(RANDOM.nextDouble() * 10);
                        int samplingRate = RANDOM.nextInt(44100);
                        int resolution = RANDOM.nextInt(1080);
                        Duration availability = Duration.ofDays(RANDOM.nextInt(30) + 1);

                        synchronized (manager) {
                            while (manager.getCurrentTotalSize() + size > maxCapacity) {
                                System.out.println(Thread.currentThread().getName() + " Waiting to insert...");
                                manager.wait();
                            }
                            try {
                                manager.create(uploaderName, mediaType, tags, size, cost, samplingRate, resolution, availability);
                                System.out.println(Thread.currentThread().getName() + " Media added.");
                            } catch (IllegalArgumentException e) {
                                System.out.println(Thread.currentThread().getName() + " Failed to add media: " + e.getMessage());
                            }
                            manager.notifyAll();
                        }

                        // Simulate time for insertion process
                        Thread.sleep(RANDOM.nextInt(1000));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Create delete threads
        for (int i = 0; i < numberOfThreads; i++) {
            deleteExecutor.submit(() -> {
                try {
                    while (true) {
                        synchronized (manager) {
                            while (manager.contentMap.isEmpty()) {
                                System.out.println(Thread.currentThread().getName() + " Waiting to delete...");
                                manager.wait();
                            }

                            String mediaToDelete = manager.contentMap.entrySet().stream()
                                    .min(Comparator.comparingLong(e -> e.getValue().getAccessCount()))
                                    .map(Map.Entry::getKey)
                                    .orElse(null);

                            if (mediaToDelete != null) {
                                manager.deleteMedia(mediaToDelete);
                                System.out.println(Thread.currentThread().getName() + " Media deleted.");
                            }
                            manager.notifyAll();
                        }

                        // Simulate time for deletion process
                        Thread.sleep(RANDOM.nextInt(1000));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Shutdown hook to gracefully shutdown executors
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            insertExecutor.shutdown();
            deleteExecutor.shutdown();
            statusExecutor.shutdown();
            try {
                insertExecutor.awaitTermination(1, TimeUnit.MINUTES);
                deleteExecutor.awaitTermination(1, TimeUnit.MINUTES);
                statusExecutor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }
}
