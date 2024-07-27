package cliPack;

import contract.Tag;
import domainLogic.Manager;
import eventSystem.EventDispatcher;
import eventSystem.UploaderEvent;
import eventSystem.MediaEvent;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    private Manager manager;
    private EventDispatcher eventDispatcher;
    private Scanner scanner = new Scanner(System.in);

    public Menu(Manager manager, EventDispatcher eventDispatcher) {
        this.manager = manager;
        this.eventDispatcher = eventDispatcher;
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

        // Fire event for uploader creation
        eventDispatcher.fireUploaderCreated(new UploaderEvent(this, uploaderName));

        while (true) {
            System.out.println("Enter media details (mediaType uploaderName size cost [samplingRate] [resolution] [tags]) or type 'done' to finish:");
            String mediaDetails = scanner.nextLine().trim();

            if (mediaDetails.equalsIgnoreCase("done")) {
                break; // Exit the loop and return to the menu
            }

            // Split the input details
            String[] details = mediaDetails.split(" ");
            if (details.length < 4) {
                System.out.println("Invalid media details format. Minimum format is: mediaType uploaderName size cost");
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
                // Parse size and cost
                size = Long.parseLong(details[2]);
                cost = new BigDecimal(details[3]);

                // Parse samplingRate and resolution based on media type
                if (mediaType.equalsIgnoreCase("Audio")) {
                    if (details.length < 5) {
                        System.out.println("Missing sampling rate for Audio media.");
                        continue;
                    }
                    samplingRate = Integer.parseInt(details[4]);
                } else if (mediaType.equalsIgnoreCase("Video") || mediaType.equalsIgnoreCase("AudioVideo")) {
                    if (details.length < 5) {
                        System.out.println("Missing resolution for Video/AudioVideo media.");
                        continue;
                    }
                    resolution = Integer.parseInt(details[4]);

                    if (mediaType.equalsIgnoreCase("AudioVideo")) {
                        if (details.length < 6) {
                            System.out.println("Missing sampling rate for AudioVideo media.");
                            continue;
                        }
                        samplingRate = Integer.parseInt(details[5]);
                    }
                }

                // Parse tags if present
                if (details.length > (mediaType.equalsIgnoreCase("AudioVideo") ? 6 : 5)) {
                    String tagInput = details[mediaType.equalsIgnoreCase("AudioVideo") ? 6 : 5];
                    if (!tagInput.trim().isEmpty()) {
                        String[] tagStrings = tagInput.split(",");
                        for (String tagStr : tagStrings) {
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
            } catch (IllegalArgumentException e) {
                System.out.println("Error parsing media details: " + e.getMessage());
                continue; // Prompt for media details again
            }

            // Fire event for media creation
            String address = String.valueOf(System.currentTimeMillis()); // Generate a unique address
            eventDispatcher.fireMediaCreated(new MediaEvent(this, address));
        }
    }

    private void handleDelete() {
        System.out.println("Enter uploader name or media address to delete:");
        String input = scanner.nextLine().trim();

        if (manager.getAllUploaders().contains(input)) {
            // Fire event for uploader deletion
            eventDispatcher.fireUploaderDeleted(new UploaderEvent(this, input));
        } else {
            // Fire event for media deletion
            eventDispatcher.fireMediaDeleted(new MediaEvent(this, input));
        }
    }

    private void handleRead() {
        System.out.println("Enter read criteria (content/tag/uploader/mediaType):");
        String criteria = scanner.nextLine().trim();

        // Fire appropriate read events here if needed
        // For now, just perform the read directly on the manager
        if (criteria.equalsIgnoreCase("content")) {
            // Display media content details
            manager.read().forEach(System.out::println);

        } else if (criteria.equalsIgnoreCase("tag")) {
            System.out.println("Tag status:");
            manager.readByTag().forEach((tag, used) ->
                    System.out.println("Tag: " + tag + " | Status: " + (used ? "Used" : "Not Used"))
            );

        } else if (criteria.equalsIgnoreCase("uploader")) {
            System.out.println("Media counts by uploader:");
            manager.readByUploader().forEach((uploader, count) ->
                    System.out.println(uploader + ": " + count)
            );

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

        // Implement persistence commands if needed
        if (command.equalsIgnoreCase("save")) {
            // Save state
        } else if (command.equalsIgnoreCase("load")) {
            // Load state
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
