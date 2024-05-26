package general;

import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;
import domainLogic.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;

class createCommand {
    private final String CurrentUser;
    public String type;
    public MediaContent mediaContent;
    List<MediaContent> userMediaList;

     public createCommand(String CurrentUser,List<MediaContent> userMediaList, String type) {
         this.CurrentUser = CurrentUser;
         this.type = type;
         this.userMediaList = userMediaList;

     }

    private MediaContent createUserMedia(String type) {
        Random random = new Random();
        String address = String.valueOf(userMediaList.size());
        long size = random.nextInt(15);

        switch (type) {
            case "audio":
                int samplingRate = random.nextInt(10) + 1;
               // mediaContent = new AudioImpl(samplingRate, address, size, CurrentUser);
                System.out.println("Audio file added.");
                break;
            case "video":
                int resolution = random.nextInt(10) + 1;
                //mediaContent = new VideoImpl(resolution, address, size, CurrentUser);
                System.out.println("Video file added.");
                break;
            default:
                System.out.println("Invalid media type.");
                mediaContent = null;
        }
        return mediaContent;
    }

    private Uploadable createUploadable(){
        Uploader uploader = new UploaderImpl(CurrentUser);
        Random random = new Random();
        Duration availability = Duration.ofHours(random.nextInt(24) + 1);
        BigDecimal cost = BigDecimal.valueOf(random.nextDouble() * 100);
        return new UploadableImpl(uploader, availability, cost);
    }
    public Uploadable getUploadable() {return createUploadable();}

    public MediaContent getMediaContent() {
        return createUserMedia(type);
    }

    private Uploader createUploader() {
         return null;
    }

}
