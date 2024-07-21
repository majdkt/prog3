package cliPack;

import all.JosCommands;
import domainLogic.Manager;
import contract.Tag;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    private Manager manager;
    private JosCommands josCommands = new JosCommands();
    private final String currentUserName;

    public Menu(Manager manager, String currentUserName) {
        this.manager = manager;
        this.currentUserName = currentUserName; // Store the name of the current user
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    handleCreateMedia(scanner);
                    break;
                case 2:
                    manager.read().forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Enter address number to update (e.g., '1' for 'address_1'):");
                    String updateAddress = "address_" + scanner.nextLine();
                    System.out.println("Enter new access count:");
                    long newAccessCount = Long.parseLong(scanner.nextLine());
                    manager.updateAccessCount(updateAddress, newAccessCount);
                    break;
                case 4:
                    System.out.println("Enter address number to delete (e.g., '1' for 'address_1'):");
                    String deleteAddress = "address_" + scanner.nextLine();
                    manager.deleteMedia(deleteAddress);
                    break;
                case 5:
                    try {
                        josCommands.saveState(manager);
                        System.out.println("State saved and can be loaded ");
                    } catch (IOException e) {
                        System.out.println("Failed to save state: " + e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        this.manager = josCommands.loadState();
                        System.out.println("State loaded. ");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Failed to load state: " + e.getMessage());
                    }
                    break;
                case 7:
                    manager.logout();
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

    private void handleCreateMedia(Scanner scanner) {
        System.out.println("Choose media type (1 for Audio, 2 for Video):");
        int mediaChoice = Integer.parseInt(scanner.nextLine());
        String mediaType;
        switch (mediaChoice) {
            case 1:
                mediaType = "Audio";
                break;
            case 2:
                mediaType = "Video";
                break;
            default:
                System.out.println("Invalid media type.");
                return;
        }

        System.out.println("Enter tags (comma separated, e.g., 'Animal,Music'):");
        String tagsInput = scanner.nextLine();
        Set<Tag> tags = new HashSet<>();
        if (!tagsInput.trim().isEmpty()) {
            String[] tagsArray = tagsInput.split(",");
            for (String tagStr : tagsArray) {
                try {
                    tags.add(Tag.valueOf(tagStr.trim()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid tag: " + tagStr);
                }
            }
        }

        try {
            manager.uploadMedia(currentUserName, mediaType, tags);
            System.out.println("Media file created successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating media: " + e.getMessage());
        }
    }
}
