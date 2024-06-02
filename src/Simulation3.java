import java.util.Scanner;

public class Simulation3 {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 4; i++) {
            menu.createMedia();
        }
        System.out.println("Before delete:");
        menu.readMedia();
        System.out.println("Enter address number to delete (e.g., '2' for 'address_2'):");
        System.out.println("For the simulation we use adress_1");
        String deleteAddress = "address_1";
        menu.deleteMedia(deleteAddress);
        System.out.println("After delete:");
        menu.readMedia();

        scanner.close();
    }
}
