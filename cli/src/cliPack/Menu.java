package cliPack;

import contract.Tag;
import eventSystem.EventDispatcher;
import eventSystem.events.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

public class Menu {
    private EventDispatcher eventDispatcher;
    private Scanner scanner = new Scanner(System.in);

    public Menu(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    private void processCommand(String command) {
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
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Type :h for help.");
                break;
        }
    }

    public void run() {
        while (true) {
            System.out.println("Enter command (type :h for help):");
            String command = scanner.nextLine().trim();
            processCommand(command);
        }
    }
    private void handleCreate() {
        System.out.println("uploader or (mediaType uploaderName size cost [samplingRate] [resolution] [tags]), or type 'done' to finish:");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.startsWith(":")) {
                processCommand(input);
                return;
            }

            String[] details = input.split(" ");

            if (details.length == 1) {
                // Single input, assume it's an uploader name
                String uploaderName = details[0];
                CheckUploaderExistenceEvent checkEvent = new CheckUploaderExistenceEvent(uploaderName);
                eventDispatcher.dispatch(checkEvent);

                if (!checkEvent.exists()) {
                    eventDispatcher.dispatch(new CreateUploaderEvent(uploaderName));
                }
            } else {
                // More inputs, assume it's media details
                if (details.length < 4) {
                    System.out.println("Invalid media details format. Minimum format is: mediaType uploaderName size cost SamplingRate/Resolution");
                    continue; // Prompt for media details again
                }

                String mediaType = details[0];
                String uploaderName = details[1];
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

                        if (mediaType.equalsIgnoreCase("AudioVideo") && details.length >= 6) {
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
                }

                CheckUploaderExistenceEvent checkEvent = new CheckUploaderExistenceEvent(uploaderName);
                eventDispatcher.dispatch(checkEvent);

                if (!checkEvent.exists()) {
                    eventDispatcher.dispatch(new CreateUploaderEvent(uploaderName));
                }

                // Dispatch create media event
                eventDispatcher.dispatch(new CreateMediaEvent(uploaderName, mediaType, tags, size, cost, samplingRate, resolution, null));
            }
        }

    }

    private void handleDelete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter uploader name or media address to delete:");
        String input = scanner.nextLine();

        if (isNumeric(input)) {
            eventDispatcher.dispatch(new DeleteEvent(input));
        } else {
            eventDispatcher.dispatch(new DeleteUploaderEvent(input));
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // Regex to check if the string is a number
        return Pattern.matches("\\d+", str);
    }

    private void handleRead() {
        System.out.println("(content <type>, tag used(i), tag unused(e), uploader, content <type>) ");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.startsWith(":")) {
                processCommand(input);
                return;
            }

            String[] parts = input.split(" ");
            String criteria = parts[0].toLowerCase();

            switch (criteria) {
                case "content":
                    if (parts.length == 2) {
                        String contentType = parts[1].trim();
                        if (contentType.equalsIgnoreCase("Audio") || contentType.equalsIgnoreCase("Video") || contentType.equalsIgnoreCase("AudioVideo")) {
                            eventDispatcher.dispatch(new ReadByMediaTypeEvent(contentType));
                        } else {
                            System.out.println("Invalid content type. Valid types are: Audio, Video, AudioVideo.");
                        }
                    } else {
                        eventDispatcher.dispatch(new ReadContentEvent());
                    }
                    break;
                case "tag":
                    if (parts.length == 2) {
                        String tagType = parts[1].trim();
                        if (tagType.equalsIgnoreCase("i")) {
                            eventDispatcher.dispatch(new ReadUsedTagsEvent());
                        } else if (tagType.equalsIgnoreCase("e")) {
                            eventDispatcher.dispatch(new ReadUnusedTagsEvent());
                        }
                    }

                    break;
                case "uploader":
                    eventDispatcher.dispatch(new ReadByUploaderEvent());
                    break;

                default:
                    System.out.println("Invalid criteria.");
                    break;
            }
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
        System.out.println(":r - Read content by criteria (content/tag/uploader/mediatype).");
        System.out.println(":u - Update media access count.");
        System.out.println(":p - Persistence (save/load state).");
        System.out.println(":h - Help.");
        System.out.println(":q - Quit.");
    }
}
