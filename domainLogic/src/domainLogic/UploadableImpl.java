package domainLogic;

import contract.Uploadable;
import contract.Uploader;

import java.math.BigDecimal;
import java.time.Duration;

public class UploadableImpl implements Uploadable {
    private Uploader uploader;
    private Duration availability;
    private BigDecimal cost;

    public UploadableImpl(Uploader uploader, Duration availability, BigDecimal cost) {
        this.uploader = uploader;
        this.availability = availability;
        this.cost = cost;
    }

    @Override
    public Uploader getUploader() {
        return this.uploader;
    }

    @Override
    public Duration getAvailability() {
        return this.availability;
    }

    @Override
    public BigDecimal getCost() {
        return this.cost;
    }
}
