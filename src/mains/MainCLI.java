package mains;

import domainLogic.Manager;
import eventSystem.EventDispatcher;
import eventSystem.UploaderEvent;
import eventSystem.MediaEvent;
import eventSystem.listeners.UploaderListener;
import eventSystem.listeners.MediaListener;

public class MainCLI {
    public static void main(String[] args) {
        Manager manager = new Manager(1000);
        EventDispatcher eventDispatcher = new EventDispatcher();

        // Add event listeners
        eventDispatcher.addUploaderListener(new UploaderListener() {
            @Override
            public void uploaderCreated(UploaderEvent event) {
                try {
                    manager.createUploader(event.getUploaderName());
                    System.out.println("Uploader created: " + event.getUploaderName());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error creating uploader: " + e.getMessage());
                }
            }

            @Override
            public void uploaderDeleted(UploaderEvent event) {
                try {
                    manager.deleteUploader(event.getUploaderName());
                    System.out.println("Uploader deleted: " + event.getUploaderName());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error deleting uploader: " + e.getMessage());
                }
            }
        });

        eventDispatcher.addMediaListener(new MediaListener() {
            @Override
            public void mediaCreated(MediaEvent event) {
                // Handle media creation logic
                System.out.println("Media created with address: " + event.getAddress());
            }

            @Override
            public void mediaDeleted(MediaEvent event) {
                manager.deleteMedia(event.getAddress());
                System.out.println("Media deleted with address: " + event.getAddress());
            }
        });

        // Create and run the menu
        cliPack.Menu menu = new cliPack.Menu(manager, eventDispatcher);
        menu.run();
    }
}
