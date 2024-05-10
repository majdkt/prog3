package general;

import contract.MediaContent;
import contract.Uploadable;
import domainLogic.Manager;

 class createCommand {

     private final Manager manager;
     private final String type;
     public createCommand(Manager manager, String type) {
         this.manager = manager;
         this.type = type;
     }

     public void run(){
        MediaContent toAdd = manager.createSampleMediaContent(type);
        Uploadable uploadable = manager.createSampleUploadable();
        manager.create(uploadable.getUploader().getName(),toAdd,uploadable);
        System.out.println("Successfully created media content.");
    }

}
