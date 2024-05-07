package domainLogic;

import contract.Uploadable;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;

 class UploadableImpl implements Uploadable {

     private Uploader uploader;
     Duration availability;
     BigDecimal cost;


     public UploadableImpl(Uploader uploader, String uploaderName) {
         this.uploader = uploader;
     }

     @Override
    public Uploader getUploader() {return this.uploader;}

    @Override
    public Duration getAvailability() {return this.availability;}

    @Override
    public BigDecimal getCost() {return this.cost;}

}
