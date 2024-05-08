package general;

import contract.MediaContent;
import contract.Uploadable;
import domainLogic.*;
import java.util.List;
import java.util.Scanner;

public class CLIControl {
    private final Manager manager;
    private final Scanner scanner;
    private String name;

    public CLIControl(Manager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Enter your name: ");
        name = scanner.nextLine().trim().toLowerCase();
        System.out.println("Welcome " + name + "!");
        System.out.println();

        boolean running = true;

        while (running) {
            System.out.println("Enter command number\n 1.create\n 2.read\n 3.update\n 4.delete\n 0.exit\n");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "1":
                    System.out.print("Type of content (audio/video): ");
                    String type = scanner.nextLine().trim().toLowerCase();
                    new create1().run(manager,name,type);
                    break;
                case "2":
                    new read1().run(manager);
                    break;
                case "3":
                    updateCommand();
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


    private void updateCommand() {
        List<MediaContent> userMediaList = manager.read();

        if (userMediaList.isEmpty()) {
            System.out.println("No media content found.");
        } else {
            System.out.println("Your media content:");
            for (int i = 0; i < userMediaList.size(); i++) {
                MediaContent mediaContent = userMediaList.get(i);
                System.out.println((i + 1) + ". " + mediaContent.getAddress());
            }

            System.out.println("Enter the index of the content you want to update:");
            int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (selectedIndex < 0 || selectedIndex >= userMediaList.size()) {
                System.out.println("Invalid index.");
                return;
            }

            MediaContent selectedContent = userMediaList.get(selectedIndex);
            System.out.println("Enter the attribute you want to update (sampling rate, address, size):");
            String attribute = scanner.nextLine().trim().toLowerCase();

            switch (attribute) {
                case "sampling rate":
                    if (selectedContent instanceof AudioImpl) {
                        System.out.println("Enter the new sampling rate:");
                        int newSamplingRate = Integer.parseInt(scanner.nextLine());
                        ((AudioImpl) selectedContent).updateSamplingRate(newSamplingRate);
                    } else {
                        System.out.println("This attribute is not applicable for the selected content type.");
                    }
                    break;
                case "resolotuion":
                    if (selectedContent instanceof VideoImpl) {
                        System.out.println("Enter the new resolution:");
                        int newResolution = Integer.parseInt(scanner.nextLine());
                        ((VideoImpl) selectedContent).updateResolution(newResolution);
                    }

                default:
                    System.out.println("Invalid attribute.");
            }

            System.out.println("Content updated successfully.");
        }
    }


    private void deleteCommand() {
        // Implement logic to remove a media file
    }

}
