package cliagain;

import domainLogic.Manager;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private final Manager manager;

    public Menu(Manager manager) {
        this.manager = manager;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    manager.create();
                    break;
                case 2:
                    manager.read().forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("Enter address number to update (e.g., '1' for 'address_1'):");
                    String updateAddress = "address_" + scanner.nextLine();
                    System.out.println("Enter new access count:");
                    long newAccessCount = Long.parseLong(scanner.nextLine());
                    manager.update(updateAddress, newAccessCount);
                    break;
                case 4:
                    System.out.println("Enter address number to delete (e.g., '1' for 'address_1'):");
                    String deleteAddress = "address_" + scanner.nextLine();
                    manager.delete(deleteAddress);
                    break;
                case 5:
                    System.out.print("Enter filename to save state: ");
                    String saveFile = scanner.nextLine();
                    try {
                        manager.saveState(saveFile);
                        System.out.println("State saved to " + saveFile);
                    } catch (IOException e) {
                        System.out.println("Failed to save state: " + e.getMessage());
                    }
                case 6:
                    System.out.print("Enter filename to load state: ");
                    String loadFile = scanner.nextLine();
                    try {
                        manager.loadState(loadFile);
                        System.out.println("State loaded from " + loadFile);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Failed to load state: " + e.getMessage());
                    }
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
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}
