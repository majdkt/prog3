package domainLogic;

import contract.Uploader;

public class UploaderImpl implements Uploader {
    private String name;

    public UploaderImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
