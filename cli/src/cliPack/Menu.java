package cliPack;

import all.JosCommands;
import domainLogic.Manager;
import domainLogic.UploaderImpl;
import contract.Tag;
import events.*;
import listeners.ManagerEventListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Menu {
    private Manager manager;
    private JosCommands josCommands = new JosCommands();
    private String uploaderName;

    public Menu(Manager manager, String uploaderName) {
        this.manager = manager;
        this.uploaderName = uploaderName;

        // Add event listeners
        manager.addEventListener(new ManagerEventListener());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Select media type: 1 for Audio, 2 for Video, 3 for AudioVideo");
                    int mediaTypeChoice = Integer.parseInt(scanner.nextLine());
                    String mediaType = mediaTypeChoice == 1 ? "Audio" : (mediaTypeChoice == 2 ? "Video" : "AudioVideo");

                    System.out.println("Enter optional tags (comma separated):");
                    String tagInput = scanner.nextLine();
                    Set<Tag> tags = new HashSet<>();
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

                    System.out.println("Enter media size (in bytes):");
                    long size = Long.parseLong(scanner.nextLine());

                    System.out.println("Enter cost:");
                    BigDecimal cost = new BigDecimal(scanner.nextLine());

                    try {
                        manager.create(uploaderName, mediaType, tags);
                        System.out.println("Media file saved.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Display media by type (leave blank for all):");
                    String mediaTypeFilter = scanner.nextLine().trim();
                    manager.read1(mediaTypeFilter.isEmpty() ? null : mediaTypeFilter)
                            .forEach(System.out::println);
                    break;

                case 3:
                    System.out.println("Enter address number to update: ");
                    String updateAddress = scanner.nextLine();
                    manager.updateAccessCount(updateAddress);
                    break;

                case 4:
                    System.out.println("Enter address number to delete: ");
                    String deleteAddress = scanner.nextLine();
                    manager.deleteMedia(deleteAddress);
                    break;

                case 5:
                    try {
                        josCommands.saveState(manager);
                        System.out.println("State saved.");
                    } catch (IOException e) {
                        System.out.println("Failed to save state: " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        this.manager = josCommands.loadState();
                        System.out.println("State loaded.");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Failed to load state: " + e.getMessage());
                    }
                    break;

                case 7:
                    manager.logout();
                    System.out.println("Logged out.");
                    break;

                case 0:
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("1. Create Media");
        System.out.println("2. Read Media");
        System.out.println("3. Update Media");
        System.out.println("4. Delete Media");
        System.out.println("5. Save data set");
        System.out.println("6. Load data set");
        System.out.println("7. LogOut");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

}
