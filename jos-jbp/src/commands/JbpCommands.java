package commands;

import domainLogic.Manager;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class JbpCommands {
    public synchronized void saveStateJBP(Manager manager) throws IOException {
        String filename = "SavedJBP.xml";
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)))) {
            encoder.writeObject(manager);
        }
    }
    public synchronized Manager loadStateJBP() throws IOException {
        String filename = "SavedJBP.xml";
        Manager manager;
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)))) {
            manager = (Manager) decoder.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            manager = null;
        }
        return manager;
    }
}
