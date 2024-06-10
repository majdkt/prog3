package contract;

import java.io.Serializable;

public interface Audio extends MediaContent,Uploadable, Serializable {
    int getSamplingRate();
}
