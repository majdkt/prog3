package general;

import contract.MediaContent;
import domainLogic.*;
import java.util.List;
import java.util.Scanner;

public class CLIControl {
    private final Manager manager;
    private final Scanner scanner;

    private final String username;

    public CLIControl(Manager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
        this.username = this.manager.getCurrentUser();
    }

    public void run() {
        System.out.println("Welcome " + username + "!");
        System.out.println();

        boolean running = true;

        while (running) {
            System.out.println("Enter command number\n 1.create\n 2.read\n 3.update\n 4.delete\n 0.exit\n");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "1":
                    System.out.print("Type of content (audio/video): ");
                    String type = scanner.nextLine().trim().toLowerCase();
                    new createCommand(manager,type).run();
                    break;
                case "2":
                    new read1(manager).run();
                    break;
                case "3":
                    new updateCommand(manager).run();
                    break;
                case "4":
                    deleteCommand();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }





    private void deleteCommand() {
        // Implement logic to remove a media file
    }

}
