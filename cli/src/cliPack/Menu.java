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
        this.scanner = new Scanner(System.in);
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
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                return;
            }

            if (input.startsWith(":")) {
                processCommand(input);
                return;
            }

            // Check if input is a single word (assumed to be an uploader name)
            String[] details = input.split(" ", 6);
            if (details.length == 1) {
                String uploaderName = details[0];
                CheckUploaderExistenceEvent checkEvent = new CheckUploaderExistenceEvent(uploaderName);
                eventDispatcher.dispatch(checkEvent);

                if (!checkEvent.exists()) {
                    eventDispatcher.dispatch(new CreateUploaderEvent(uploaderName));
                }
                continue;
            }

            // Split into first 5 parts, then handle optional parameters separately
            if (details.length < 5) {
                System.out.println("Invalid media details format. Minimum format is: [Media-Typ] [P-Name] [kommaseparierte Tags, einzelnes Komma für keine] [Größe] [Abrufkosten] [[Optionale Parameter]]");
                continue;
            }

            String mediaType = details[0];
            String uploaderName = details[1];
            String tagsInput = details[2];
            long size;
            BigDecimal cost;

            try {
                size = Long.parseLong(details[3].replace(",", ""));
                cost = new BigDecimal(details[4].replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in media details.");
                continue;
            }

            Set<Tag> tags = new HashSet<>();
            if (!tagsInput.equals(",")) {
                String[] tagStrings = tagsInput.split(",");
                for (String tagStr : tagStrings) {
                    try {
                        Tag tag = Tag.valueOf(tagStr.trim());
                        tags.add(tag);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid tag: " + tagStr.trim());
                    }
                }
            }

            int samplingRate = 0; // No default value
            int resolution = 0; // No default value

            // Handling optional parameters
            if (details.length == 6) {
                String[] optionalParams = details[5].split(" ");
                try {
                    if (mediaType.equalsIgnoreCase("Audio")) {
                        if (optionalParams.length >= 1) {
                            samplingRate = Integer.parseInt(optionalParams[0]);
                        } else {
                            System.out.println("Missing sampling rate for Audio media.");
                            continue;
                        }
                    } else if (mediaType.equalsIgnoreCase("Video")) {
                        if (optionalParams.length >= 1) {
                            resolution = Integer.parseInt(optionalParams[0]);
                        } else {
                            System.out.println("Missing resolution for Video media.");
                            continue;
                        }
                    } else if (mediaType.equalsIgnoreCase("AudioVideo")) {
                        if (optionalParams.length >= 1) {
                            resolution = Integer.parseInt(optionalParams[0]);
                        } else {
                            System.out.println("Missing resolution for AudioVideo media.");
                            continue;
                        }
                        if (optionalParams.length >= 2) {
                            samplingRate = Integer.parseInt(optionalParams[1]);
                        } else {
                            System.out.println("Missing sampling rate for AudioVideo media.");
                            continue;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in optional parameters.");
                    continue;
                }
            }

            CheckUploaderExistenceEvent checkEvent = new CheckUploaderExistenceEvent(uploaderName);
            eventDispatcher.dispatch(checkEvent);

            if (!checkEvent.exists()) {
                eventDispatcher.dispatch(new CreateUploaderEvent(uploaderName));
            }

            eventDispatcher.dispatch(new CreateMediaEvent(uploaderName, mediaType, tags, size, cost, samplingRate, resolution, null));
        }
    }


    private void handleDelete() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (isNumeric(input)) {
            eventDispatcher.dispatch(new DeleteEvent(input));
        } else {
            eventDispatcher.dispatch(new AlternativCLIEvent());
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
                    eventDispatcher.dispatch(new AlternativCLIEvent());
                    if (parts.length == 2) {
                        String tagType = parts[1].trim();
                        if (tagType.equalsIgnoreCase("i")) {
                            eventDispatcher.dispatch(new ReadUsedTagsEvent());
                        } else if (tagType.equalsIgnoreCase("e")) {
                            eventDispatcher.dispatch(new ReadUnusedTagsEvent());
                        }
                    }
                    if (parts.length == 1) {
                        eventDispatcher.dispatch(new ReadByTagEvent());
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
        System.out.println("Enter persistence command (save [JOS|JBP]/load [JOS|JBP]):");
        String command = scanner.nextLine().trim();
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            System.out.println("Invalid persistence command format. Use save [JOS|JBP] or load [JOS|JBP].");
            return;
        }

        String action = parts[0].toLowerCase();
        String technology = parts[1].toUpperCase();

        switch (action) {
            case "save":
                if (technology.equals("JOS")) {
                    eventDispatcher.dispatch(new SaveStateJOSEvent());
                } else if (technology.equals("JBP")) {
                    eventDispatcher.dispatch(new SaveStateJBPEvent());
                } else {
                    System.out.println("Unknown persistence technology: " + technology);
                }
                break;
            case "load":
                if (technology.equals("JOS")) {
                    eventDispatcher.dispatch(new LoadStateJOSEvent());
                } else if (technology.equals("JBP")) {
                    eventDispatcher.dispatch(new LoadStateJBPEvent());
                } else {
                    System.out.println("Unknown persistence technology: " + technology);
                }
                break;
            default:
                System.out.println("Invalid persistence command.");
                break;
        }
    }

    private void showHelp() {
        System.out.println("Command list:");
        System.out.println(":c - Create uploader and media files. [optional] To end media input, type 'done'.");
        System.out.println(":d - Delete uploader or media.");
        System.out.println(":r - Read content by criteria (content/tag/uploader/mediatype).");
        System.out.println(":u - Update media access count.");
        System.out.println(":p - Persistence (save/load state).");
        System.out.println(":h - Help.");
        System.out.println(":q - Quit.");
    }
}
