package all;

import domainLogic.Manager;

import java.io.*;

public class JosCommands {

    public synchronized void saveState(Manager manager) throws IOException {
        String filename = "Saved";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(manager);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized Manager loadState() throws IOException, ClassNotFoundException {
        String filename = "Saved";
        Manager manager;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            manager = (Manager) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
            manager = null;
        }
        return manager;
    }
}

