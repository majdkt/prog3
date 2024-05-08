package general;

import contract.MediaContent;
import contract.Uploadable;
import domainLogic.Manager;

 class create1 {
    public void run(Manager manager, String name, String type){
        MediaContent toAdd = manager.createSampleMediaContent(name,type);
        Uploadable uploadable = manager.createSampleUploadable(name);
        manager.create(toAdd,uploadable);
        System.out.println("Successfully created media content.");
    }

}
