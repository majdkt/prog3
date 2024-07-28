package mains;

import domainLogic.Manager;
import domainLogic.AudioImpl;
import domainLogic.VideoImpl;
import domainLogic.AudioVideoImpl;
import domainLogic.UploaderImpl;
import contract.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ThreadSafetySimulation {

    // Simulationszeitdauer in Millisekunden für den Test
    private static final long SLEEP_DURATION = 500;

    public static void main(String[] args) {
        // Überprüfen der Kommandozeilenargumente
        if (args.length != 1) {
            System.err.println("Usage: java ThreadSafetySimulation <maxCapacity>");
            System.exit(1);
        }

        // Kapazität aus den Kommandozeilenargumenten lesen
        long maxCapacity;
        try {
            maxCapacity = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid capacity. Please provide a valid number.");
            System.exit(1);
            return; // Dies wird nie erreicht, aber notwendig für den Compiler
        }

        // Manager-Instanz erstellen
        Manager manager = new Manager(maxCapacity);

        // Threads für die Simulation der Operationen erstellen
        Thread uploaderThread1 = new Thread(() -> simulateUploaderOperations(manager, "Uploader1"));
        Thread uploaderThread2 = new Thread(() -> simulateUploaderOperations(manager, "Uploader2"));
        Thread uploaderThread3 = new Thread(() -> simulateUploaderOperations(manager, "Uploader3"));

        // Threads starten
        uploaderThread1.start();
        uploaderThread2.start();
        uploaderThread3.start();

        // Auf Abschluss der Threads warten
        try {
            uploaderThread1.join();
            uploaderThread2.join();
            uploaderThread3.join();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }

        // Abschlussnachricht
        System.out.println("Simulation completed.");
    }

    private static void simulateUploaderOperations(Manager manager, String uploaderName) {
        try {
            // Erstellen eines Uploader
            manager.createUploader(uploaderName);
            System.out.println(uploaderName + " created.");

            // Hochladen von Medien
            for (int i = 0; i < 5; i++) {
                String mediaType = i % 3 == 0 ? "Audio" : (i % 3 == 1 ? "Video" : "AudioVideo");
                long size = 1000 + i * 500;
                BigDecimal cost = BigDecimal.valueOf(10 + i);
                Duration availability = Duration.ofDays(30);
                Set<Tag> tags = new HashSet<>();
                tags.add(Tag.News);

                try {
                    manager.create(uploaderName, mediaType, tags, size, cost, 44100, 1080, availability);
                    System.out.println(uploaderName + " uploaded " + mediaType + " media with size " + size + ".");
                } catch (IllegalArgumentException e) {
                    System.err.println(uploaderName + " failed to upload " + mediaType + " media: " + e.getMessage());
                }

                // Simulierungspause
                Thread.sleep(SLEEP_DURATION);
            }

            // Lesen aller Medien
            System.out.println("Reading all media by " + uploaderName + ":");
            manager.read().forEach(System.out::println);

            // Zugriffszähler für einige Medien aktualisieren
            manager.read().forEach(mediaDetail -> {
                String address = extractAddressFromMediaDetail(mediaDetail);
                manager.updateAccessCount(address);
                System.out.println(uploaderName + " updated access count for media at address " + address + ".");
            });

            // Löschen einiger Medien
            manager.read().forEach(mediaDetail -> {
                String address = extractAddressFromMediaDetail(mediaDetail);
                manager.deleteMedia(address);
                System.out.println(uploaderName + " deleted media at address " + address + ".");
            });

            // Logout
            manager.logout();
            System.out.println(uploaderName + " logged out.");
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }

    private static String extractAddressFromMediaDetail(String mediaDetail) {
        // Extrahiert die Adresse aus der Medienbeschreibung
        try {
            return mediaDetail.split("\\[")[1].split(",")[0].split(":")[1].trim();
        } catch (Exception e) {
            return "unknownAddress";
        }
    }
}
