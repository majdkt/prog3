package general;

import contract.MediaContent;
import domainLogic.AudioImpl;
import domainLogic.Manager;
import domainLogic.VideoImpl;

import java.util.List;

class readCommand {
    private final Manager manager;

    public readCommand(Manager manager) {
        this.manager = manager;
    }


    public void run() {
        List<MediaContent> userMediaList = manager.read();

        if (userMediaList.isEmpty()) {
            System.out.println("No media content found.");
        } else {
            System.out.println("Your media content:");

            for (MediaContent mediaContent : userMediaList) {
                String contentType;
                if (mediaContent instanceof AudioImpl) {
                    contentType = "Audio";
                } else if (mediaContent instanceof VideoImpl) {
                    contentType = "Video";
                } else {
                    contentType = "Unknown";
                }

                System.out.println("Type: " + contentType);
                System.out.println("Address: " + mediaContent.getAddress());
                System.out.println("Size: " + mediaContent.getSize());

                if (mediaContent instanceof AudioImpl) {
                    AudioImpl audio = (AudioImpl) mediaContent;
                    System.out.println("Sampling Rate: " + audio.getSamplingRate());
                } else if (mediaContent instanceof VideoImpl) {
                    VideoImpl video = (VideoImpl) mediaContent;
                    System.out.println("Resolution: " + video.getResolution());
                }

                System.out.println();
            }

        }
    }
}
