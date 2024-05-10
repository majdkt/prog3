package general;

import contract.MediaContent;
import domainLogic.Manager;

import java.util.List;

class updateCommand {
    private final Manager manager;

    updateCommand(Manager manager) {
        this.manager = manager;
    }

    public void run() {
        // Obtain user's media list from the manager
        List<MediaContent> userMediaList = manager.read();

        if (userMediaList.isEmpty()) {
            System.out.println("No media content found.");
            return;
        }

        System.out.println("Your media content:");
        for (int i = 0; i < userMediaList.size(); i++) {
            MediaContent mediaContent = userMediaList.get(i);
            System.out.println((i + 1) + ". " + mediaContent.getAddress());
        }

    }
}
