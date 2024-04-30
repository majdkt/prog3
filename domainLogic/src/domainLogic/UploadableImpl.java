package domainLogic;

import contract.Uploadable;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;

public class UploadableImpl implements Uploadable {
    @Override
    public Uploader getUploader() {
        return null;
    }

    @Override
    public Duration getAvailability() {
        return null;
    }

    @Override
    public BigDecimal getCost() {
        return null;
    }
}
