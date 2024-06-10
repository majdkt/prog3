import domainLogic.Manager;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Menu implements Runnable {
    private final BlockingQueue<Command> commandQueue;
    private final Manager manager;

    public Menu(BlockingQueue<Command> commandQueue, Manager manager) {
        this.commandQueue = commandQueue;
        this.manager = manager;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            try {
                switch (choice) {
                    case 1:
                        commandQueue.put(new Command("create"));
                        break;
                    case 2:
                        commandQueue.put(new Command("read"));
                        break;
                    case 3:
                        System.out.println("Enter address number to update (e.g., '1' for 'address_1'):");
                        String updateAddress = "address_" + scanner.nextLine();
                        System.out.println("Enter new access count:");
                        long newAccessCount = Long.parseLong(scanner.nextLine());
                        commandQueue.put(new Command("update", updateAddress, newAccessCount));
                        break;
                    case 4:
                        System.out.println("Enter address number to delete (e.g., '1' for 'address_1'):");
                        String deleteAddress = "address_" + scanner.nextLine();
                        commandQueue.put(new Command("delete", deleteAddress));
                        break;
                    case 0:
                        commandQueue.put(new Command("exit"));
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Menu interrupted");
                return;
            }
        }
    }

    private void showMenu() {
        System.out.println("1. Create Media");
        System.out.println("2. Read Media");
        System.out.println("3. Update Media");
        System.out.println("4. Delete Media");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}
