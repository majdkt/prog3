package general;

import contract.MediaContent;
import domainLogic.Manager;

import java.util.List;
import java.util.Scanner;

public class updateCommand {
    private final Manager manager;

    public updateCommand(Manager manager) {
        this.manager = manager;
    }

    public void run() {
        // Obtain user's media list from the manager
        List<MediaContent> userMediaList = manager.read();

        if (userMediaList.isEmpty()) {
            System.out.println("No media content found.");
            return;
        }

        // Display user's media content
        System.out.println("Your media content:");
        for (int i = 0; i < userMediaList.size(); i++) {
            MediaContent mediaContent = userMediaList.get(i);
            System.out.println((i + 1) + ". " + mediaContent.getAddress());
        }

        // Prompt user to select content to update
        System.out.println("Enter the index of the content you want to update:");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (selectedIndex < 0 || selectedIndex >= userMediaList.size()) {
            System.out.println("Invalid index.");
            return;
        }

        // Prompt user to specify attribute and new value
        MediaContent selectedContent = userMediaList.get(selectedIndex);
        System.out.println("Enter the attribute you want to update (sampling rate, resolution):");
        String attribute = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter the new value:");
        Object value = scanner.nextInt();
        manager.update(selectedContent, attribute, value);
        System.out.println("Content updated successfully.");
    }
}
