package all;

import domainLogic.Manager;

import java.io.*;

public class JbpCommands {

    // Save the state of the Manager to a binary file
    public synchronized void saveState(Manager manager) throws IOException {
        String filename = "SavedJBP";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(manager);
        }
    }

    // Load the state of the Manager from a binary file
    @SuppressWarnings("unchecked")
    public synchronized Manager loadState() throws IOException, ClassNotFoundException {
        String filename = "SavedJBP";
        Manager manager;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            manager = (Manager) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            manager = null;
        }
        return manager;
    }
}
