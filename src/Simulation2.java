import java.util.Scanner;

public class Simulation2 {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            menu.createMedia();
        }
        System.out.println("Before update:");
        menu.readMedia();
        System.out.println("Enter address number to update (e.g., '1' for 'address_1'):");
        String updateAddress = "address_2";
        System.out.println("For the simulation we use adress_2");
        System.out.println("Enter new access count:");
        System.out.println("For the simulation we use 10 as a new access count ");
        long newAccessCount = 10;
        menu.updateMedia(updateAddress, newAccessCount);
        System.out.println("After update:");
        menu.readMedia();

        scanner.close();
    }
}
