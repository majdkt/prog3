package domainLogic;

import contract.Audio;
import contract.MediaContent;
import contract.Uploadable;

abstract class AudioImpl extends MediaContentImpl implements Audio {

    private final int samplingRate;

     AudioImpl(int samplingRate) {
         super();
         this.samplingRate = samplingRate;
     }

    @Override
    public int getSamplingRate() {return this.samplingRate;}


}
