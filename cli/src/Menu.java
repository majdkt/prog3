import domainLogic.Manager;
import java.util.Scanner;

public class Menu {
    private Manager manager;
    private Scanner scanner;

    public Menu() {
        this.manager = new Manager();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        System.out.println("1. Create Media");
        System.out.println("2. Read Media");
        System.out.println("3. Update Media");
        System.out.println("4. Delete Media");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    public void createMedia() {
        manager.create();
        System.out.println("Audio created.");
    }

    public void readMedia() {
        System.out.println("Saved audio files:");
        System.out.println(manager.read());
        //System.out.println(manager.getAudioList());
    }

    public void updateMedia() {
        System.out.println("Enter address number to update (e.g., '1' for 'address_1'):");
        String updateAddress = "address_" + scanner.nextLine();
        System.out.println("Enter new access count:");
        long newAccessCount = Long.parseLong(scanner.nextLine());
        manager.update(updateAddress, newAccessCount);
        System.out.println(updateAddress + " updated.");
    }

    public void updateMedia(String address, long newAccessCount) {
        manager.update(address, newAccessCount);
        System.out.println(address + " updated.");
    }

    public void deleteMedia() {
        System.out.println("Enter address number to delete (e.g., '1' for 'address_1'):");
        String deleteAddress = "address_" + scanner.nextLine();
        manager.delete(deleteAddress);
        System.out.println(deleteAddress + " deleted.");
    }

    public void deleteMedia(String address) {
        manager.delete(address);
        System.out.println(address + " deleted.");
    }

    public void exit() {
        System.out.println("Exiting...");
        scanner.close();
    }
}
