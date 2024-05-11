package general;

import contract.MediaContent;
import domainLogic.AudioImpl;
import domainLogic.VideoImpl;

import java.util.List;

class readCommand {
    List<MediaContent> userMediaList;
    public readCommand(List<MediaContent> userMediaList) {
        this.userMediaList = userMediaList;
    }

    public void run() {
        if (userMediaList.isEmpty()) {
            System.out.println("No media content found.");
        } else {
            System.out.println("Your media content: ");

            for (MediaContent mediaContent : userMediaList) {
                String contentType;
                if (mediaContent instanceof AudioImpl) {
                    contentType = "Audio";
                } else if (mediaContent instanceof VideoImpl) {
                    contentType = "Video";
                } else {
                    contentType = "Unknown";
                }
                System.out.print("Type: " + contentType);
                System.out.print("| Address: " + mediaContent.getAddress());
                System.out.print("| Size: " + mediaContent.getSize() + "| ");

                if (mediaContent instanceof AudioImpl) {
                    AudioImpl audio = (AudioImpl) mediaContent;
                    System.out.print("Sampling Rate: " + audio.getSamplingRate());
                } else if (mediaContent instanceof VideoImpl) {
                    VideoImpl video = (VideoImpl) mediaContent;
                    System.out.print("Resolution: " + video.getResolution());
                }

                System.out.println();
            }

        }
    }
}
