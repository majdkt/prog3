import java.util.Scanner;

public class cliMain {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            menu.showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    menu.createMedia();
                    break;
                case 2:
                    menu.readMedia();
                    break;
                case 3:
                    menu.updateMedia();
                    break;
                case 4:
                    menu.deleteMedia();
                    break;
                case 0:
                    menu.exit();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
