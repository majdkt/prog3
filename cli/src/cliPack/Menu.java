package cliPack;

import contract.Tag;
import eventSystem.EventDispatcher;
import eventSystem.events.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    private final EventDispatcher eventDispatcher;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void run() {
        while (true) {
            System.out.println("Enter command (type :h for help):");
            String command = scanner.nextLine().trim();

            switch (command) {
                case ":c":
                    handleCreate();
                    break;
                case ":d":
                    handleDelete();
                    break;
                case ":r":
                    handleRead();
                    break;
                case ":u":
                    handleUpdate();
                    break;
                case ":p":
                    handlePersistence();
                    break;
                case ":h":
                    showHelp();
                    break;
                case ":q":
                    return;
                default:
                    System.out.println("Invalid command. Type :h for help.");
                    break;
            }
        }
    }

    private void handleCreate() {
        System.out.println("Enter uploader name:");
        String uploaderName = scanner.nextLine().trim();

        // Dispatch create uploader event
        eventDispatcher.dispatch(new CreateUploaderEvent(uploaderName));

        while (true) {
            System.out.println("Enter media details (mediaType uploaderName size cost [samplingRate] [resolution] [tags]) or type 'done' to finish:");
            String mediaDetails = scanner.nextLine().trim();

            if (mediaDetails.equalsIgnoreCase("done")) {
                break; // Exit the loop and return to the menu
            }

            // Split the input details
            String[] details = mediaDetails.split(" ");
            if (details.length < 5) {
                System.out.println("Invalid media details format. Minimum format is: mediaType uploaderName size cost");
                continue; // Prompt for media details again
            }

            String mediaType = details[0];
            long size ;
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
                    if (details.length < 4) {
                        System.out.println("Missing sampling rate for Audio media.");
                        continue;
                    }
                    samplingRate = Integer.parseInt(details[4]);
                } else if (mediaType.equalsIgnoreCase("Video") || mediaType.equalsIgnoreCase("AudioVideo")) {
                    if (details.length < 4) {
                        System.out.println("Missing resolution for Video/AudioVideo media.");
                        continue;
                    }
                    resolution = Integer.parseInt(details[4]);

                    if (mediaType.equalsIgnoreCase("AudioVideo")) {
                        if (details.length < 5) {
                            System.out.println("Missing resolution rate for AudioVideo media.");
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

            // Dispatch create media event
            eventDispatcher.dispatch(new CreateMediaEvent(uploaderName, mediaType, tags, size, cost, samplingRate, resolution, null));
        }

        // Return to menu after finishing creation
    }

    private void handleDelete() {
        System.out.println("Enter uploader name or media address to delete:");
        String input = scanner.nextLine().trim();

        // Dispatch delete uploader or media event
        eventDispatcher.dispatch(new DeleteEvent(input));
    }

    private void handleRead() {
        System.out.println("Enter read criteria (content/tag/uploader/mediaType):");
        String criteria = scanner.nextLine().trim();

        switch (criteria.toLowerCase()) {
            case "content":
                // Dispatch read content event
                eventDispatcher.dispatch(new ReadContentEvent());
                break;
            case "tag":
                // Dispatch read by tag event
                eventDispatcher.dispatch(new ReadByTagEvent());
                break;
            case "uploader":
                // Dispatch read by uploader event
                eventDispatcher.dispatch(new ReadByUploaderEvent());
                break;
            case "mediaType":
                System.out.println("Enter media type (audio/video/audioVideo):");
                String mediaType = scanner.nextLine().trim();
                // Dispatch read by media type event
                eventDispatcher.dispatch(new ReadByMediaTypeEvent(mediaType));
                break;
            default:
                System.out.println("Invalid criteria.");
                break;
        }
    }

    private void handleUpdate() {
        System.out.println("Enter media address to update access count:");
        String address = scanner.nextLine().trim();
        // Dispatch update access count event
        eventDispatcher.dispatch(new UpdateAccessCountEvent(address));
    }

    private void handlePersistence() {
        System.out.println("Enter persistence command (save/load):");
        String command = scanner.nextLine().trim();

        switch (command.toLowerCase()) {
            case "save":
                // Dispatch save state event
                eventDispatcher.dispatch(new SaveStateEvent());
                break;
            case "load":
                // Dispatch load state event
                eventDispatcher.dispatch(new LoadStateEvent());
                break;
            default:
                System.out.println("Invalid persistence command.");
                break;
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
