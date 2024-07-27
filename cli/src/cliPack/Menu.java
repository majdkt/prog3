package cliPack;

import all.JosCommands;
import domainLogic.Manager;
import contract.Tag;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class Menu {
    private Manager manager;
    private JosCommands josCommands = new JosCommands();
    private Scanner scanner = new Scanner(System.in);

    public Menu(Manager manager) {
        this.manager = manager;
    }

    public void run() {
        while (true) {
            System.out.println("Enter command (type :h for help):");
            String command = scanner.nextLine().trim();

            if (command.equals(":c")) {
                handleCreate();
            } else if (command.equals(":d")) {
                handleDelete();
            } else if (command.equals(":r")) {
                handleRead();
            } else if (command.equals(":u")) {
                handleUpdate();
            } else if (command.equals(":p")) {
                handlePersistence();
            } else if (command.equals(":h")) {
                showHelp();
            } else if (command.equals(":q")) {
                break;
            } else {
                System.out.println("Invalid command. Type :h for help.");
            }
        }
    }

    private void handleCreate() {
        System.out.println("Enter uploader name:");
        String uploaderName = scanner.nextLine().trim();

        try {
            manager.createUploader(uploaderName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; // Return to the menu if there's an issue creating the uploader
        }

        while (true) {
            System.out.println("Enter media details (type mediaType uploaderName size cost samplingRate/resolution [tags]) or type 'done' to finish:");
            String mediaDetails = scanner.nextLine().trim();

            if (mediaDetails.equalsIgnoreCase("done")) {
                break; // Exit loop and return to menu
            }

            String[] details = mediaDetails.split(" ");
            if (details.length < 4) {
                System.out.println("Invalid media details format.");
                continue; // Prompt for media details again
            }

            String mediaType = details[0];
            String uploader = details[1];
            long size;
            BigDecimal cost;
            int samplingRate = 0;
            int resolution = 0;
            Set<Tag> tags = new HashSet<>();

            try {
                size = Long.parseLong(details[2]);
                cost = new BigDecimal(details[3]);

                if (mediaType.equalsIgnoreCase("Audio")) {
                    samplingRate = Integer.parseInt(details[4]);
                }
                if (mediaType.equalsIgnoreCase("Video") || mediaType.equalsIgnoreCase("AudioVideo")) {
                    resolution = Integer.parseInt(details[4]);
                    if (mediaType.equalsIgnoreCase("AudioVideo")) {
                        samplingRate = Integer.parseInt(details[5]);
                    }
                }

                // Check if tags are present
                if (details.length > (mediaType.equalsIgnoreCase("AudioVideo") ? 6 : 5)) {
                    String tagInput = details[mediaType.equalsIgnoreCase("AudioVideo") ? 6 : 5];
                    if (!tagInput.trim().isEmpty()) {
                        for (String tagStr : tagInput.split(",")) {
                            try {
                                Tag tag = Tag.valueOf(tagStr.trim());
                                tags.add(tag);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid tag: " + tagStr.trim());
                            }
                        }
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in media details.");
                continue; // Prompt for media details again
            }

            try {
                manager.create(uploader, mediaType, tags, size, cost, samplingRate, resolution, Duration.ofDays(1));
                System.out.println("Media file saved.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Return to menu after finishing creation
    }


    private void handleDelete() {
        System.out.println("Enter uploader name or media address to delete:");
        String input = scanner.nextLine().trim();

        if (manager.getAllUploaders().contains(input)) {
            try {
                manager.deleteUploader(input);
                System.out.println("Uploader deleted.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            manager.deleteMedia(input);
            System.out.println("Media deleted.");
        }
    }

    private void handleRead() {
        System.out.println("Enter read criteria (content/tag/uploader/mediaType):");
        String criteria = scanner.nextLine().trim();

        if (criteria.equalsIgnoreCase("content")) {
            // Display media content details
            manager.read().forEach(System.out::println);

        } else if (criteria.equalsIgnoreCase("tag")) {
            System.out.println("Enter tag:");
            String tagInput = scanner.nextLine().trim();
            try {
                Tag tag = Tag.valueOf(tagInput);
                manager.readByTag(tag).forEach(System.out::println);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid tag.");
            }

        } else if (criteria.equalsIgnoreCase("uploader")) {
            System.out.println("Enter uploader name:");
            String uploaderName = scanner.nextLine().trim();
            manager.readByUploader(uploaderName).forEach(System.out::println);

        } else if (criteria.equalsIgnoreCase("mediaType")) {
            System.out.println("Enter media type (Audio/Video/AudioVideo):");
            String mediaType = scanner.nextLine().trim();
            manager.readByMediaType(mediaType).forEach(System.out::println);

        } else {
            System.out.println("Invalid criteria.");
        }
    }

    private void handleUpdate() {
        System.out.println("Enter media address to update access count:");
        String address = scanner.nextLine().trim();
        manager.updateAccessCount(address);
        System.out.println("Access count updated.");
    }

    private void handlePersistence() {
        System.out.println("Enter persistence command (save/load):");
        String command = scanner.nextLine().trim();

        if (command.equalsIgnoreCase("save")) {
            try {
                josCommands.saveState(manager);
                System.out.println("State saved.");
            } catch (IOException e) {
                System.out.println("Failed to save state: " + e.getMessage());
            }
        } else if (command.equalsIgnoreCase("load")) {
            try {
                manager = josCommands.loadState();
                System.out.println("State loaded.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load state: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid persistence command.");
        }
    }

    private void showHelp() {
        System.out.println("Command list:");
        System.out.println(":c - Create uploader and media files. To end media input, type 'done'.");
        System.out.println(":d - Delete uploader or media.");
        System.out.println(":r - Read content by criteria (content/tag/uploader/mediaType).");
        System.out.println(":u - Update media access count.");
        System.out.println(":p - Persistence (save/load state).");
        System.out.println(":h - Help.");
        System.out.println(":q - Quit.");
    }
}
