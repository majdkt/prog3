package general;

import contract.MediaContent;
import contract.Uploadable;
import domainLogic.*;

import java.util.List;
import java.util.Scanner;

public class CLIControl {
    private final Manager manager;
    private final Scanner scanner;

    private final String username;

    public CLIControl(Manager manager, Scanner scanner, String username) {
        this.manager = manager;
        this.scanner = scanner;
        this.username = username;
    }


    public void run() {
        System.out.println("Welcome " + username + "!");
        System.out.println();

        boolean running = true;

        while (running) {
            List<MediaContent> userMediaList = manager.read();
            System.out.println("Enter command number:\n 1.create, 2.read, 3.update, 4.delete, 0.exit\n");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "1":
                    System.out.print("Type of content (audio/video): ");
                    String type = scanner.nextLine().trim().toLowerCase();
                    if (type.equals("audio") || type.equals("video")) {
                        MediaContent mediaContent = new createCommand(username,userMediaList,type).getMediaContent();
                        Uploadable uploadable = new createCommand(username,userMediaList,type).getUploadable();
                       // manager.create(mediaContent,uploadable);
                    } else {
                        System.out.println("Invalid type. Please enter either 'audio' or 'video'.");
                    }
                    break;
                case "2":
                    new readCommand(userMediaList).run();
                    break;
                case "3":
                    new readCommand(userMediaList).run();
                    if (!userMediaList.isEmpty()){
                        System.out.println("Choose address to edit: ");
                        int i = scanner.nextInt();
                        manager.update(i);
                        System.out.println("Access count updated! ");
                        new readCommand(userMediaList).run();
                    }
                    break;
                case "4": //delete
                    new readCommand(userMediaList).run();
                    if (!userMediaList.isEmpty())
                    {
                        System.out.println("Select address to delete: ");
                        int indx = scanner.nextInt();
                        if(indx > userMediaList.size() || indx <= 0 ){
                            System.out.println("Invalid address.");
                        }
                        else{
                            MediaContent toDelete = new deleteCommand(userMediaList,indx).getToDelete();
                            boolean deleted = manager.delete(toDelete);
                            if (deleted) {
                                System.out.println("Content deleted successfully.\n");
                            } else {
                                System.out.println("Failed to delete content.");
                            }
                        }
                    }
                    break;
                case "0":
                    running = false;
                    break;
                    default:
                    System.out.println("Invalid command.");
            }

        }
    }
}
