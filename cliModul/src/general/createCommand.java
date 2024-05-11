package general;

import contract.MediaContent;
import contract.Uploadable;
import contract.Uploader;
import domainLogic.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Random;

class createCommand {

     private final Manager manager;
     private final String type;
     public createCommand(Manager manager, String type) {
         this.manager = manager;
         this.type = type;
     }

     public void run(){
         Uploader uploader = new UploaderImpl(manager.getCurrentUser());
         Random random = new Random();
         Duration availability = Duration.ofHours(random.nextInt(24) + 1);
         BigDecimal cost = BigDecimal.valueOf(random.nextDouble() * 100);
         Uploadable uploadable = new UploadableImpl(uploader, availability, cost);

         String address = "NULL";
         long size = 0;
         MediaContent mediaContent = null;

         switch (type) {
             case "audio":
                 int samplingRate = random.nextInt(10) + 1;
                 mediaContent = new AudioImpl(samplingRate, address, size, uploader);
                 System.out.println("Audio file added.");
                 break;
             case "video":
                 int resolution = random.nextInt(10) + 1;
                 mediaContent = new VideoImpl(resolution, address, size, uploader);
                 System.out.println("Video file added.");
                 break;
             default:
                 System.out.println("Invalid media type.");
         }

         if (mediaContent != null){
             manager.read().add(mediaContent);
             manager.getTaskAssignments().put(mediaContent, uploadable);
         }



         manager.create(manager.getCurrentUser(),type);
    }

}
