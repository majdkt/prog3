package cliPack;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private MenuHandler handler;
    private Scanner scanner;

    public Menu(MenuHandler handler) {
        this.handler = handler;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        try {
            while (true) {
                showMenu();
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        handler.create();
                        System.out.println("Audio file created.");
                        break;
                    case 2:
                        handler.read().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("Enter address number to update (e.g., '1' for 'address_1'):");
                        String updateAddress = "address_" + scanner.nextLine();
                        System.out.println("Enter new access count:");
                        long newAccessCount = Long.parseLong(scanner.nextLine());
                        handler.update(updateAddress, newAccessCount);
                        System.out.println("Audio file updated.");
                        break;
                    case 4:
                        System.out.println("Enter address number to delete (e.g., '1' for 'address_1'):");
                        String deleteAddress = "address_" + scanner.nextLine();
                        handler.delete(deleteAddress);
                        System.out.println("Audio file deleted.");
                        break;
                    case 5:
                        handler.save();
                        System.out.println("State saved.");
                        break;
                    case 6:
                        handler.load();
                        System.out.println("State loaded.");
                        break;
                    case 7:
                        handler.logout();
                        System.out.println("Logged out.");
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
