package general;

import domainLogic.Manager;
import contract.MediaContent;
import java.util.List;
import java.util.Scanner;

class deleteCommand {
    private final Manager manager;

    public deleteCommand(Manager manager) {
        this.manager = manager;
    }

    public void run() {
        // Obtain user's media list from the manager
        List<MediaContent> userMediaList = manager.read();

        if (userMediaList.isEmpty()) {
            System.out.println("No media content found for user: " + manager.getCurrentUser());
            return;
        }

        System.out.println("Your media content:");
        for (int i = 0; i < userMediaList.size(); i++) {
            MediaContent mediaContent = userMediaList.get(i);
            System.out.println((i + 1) + ". " + mediaContent);
        }

        // Prompt user to select content to delete
        System.out.println("Enter the index of the content you want to delete:");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (selectedIndex < 0 || selectedIndex >= userMediaList.size()) {
            System.out.println("Invalid index.");
            return;
        }

        // Delete the selected content
        MediaContent selectedContent = userMediaList.get(selectedIndex);
        boolean deleted = manager.delete(selectedContent);
        if (deleted) {
            System.out.println("Content deleted successfully.");
        } else {
            System.out.println("Failed to delete content.");
        }
    }
}
