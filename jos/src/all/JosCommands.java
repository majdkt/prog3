package all;

import domainLogic.Manager;
import events.AudioEventHandler;
import events.AudioEventListener;
import events.EventManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JosCommands {

    public synchronized void saveState(Manager manager) throws IOException {
        String filename = "Saved";

        // Temporarily detach listeners
        EventManager eventManager = manager.getEventManager();
        List<AudioEventListener> listeners = eventManager.getListeners();
        eventManager.setListeners(new ArrayList<>());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(manager);
        } finally {
            // Reattach listeners
            eventManager.setListeners(listeners);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized Manager loadState() throws IOException, ClassNotFoundException {
        String filename = "Saved";
        Manager manager;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            manager = (Manager) ois.readObject();
        }

        // Reattach listeners after loading
        EventManager eventManager = manager.getEventManager();
        eventManager.addListener(new AudioEventHandler());

        return manager;
    }
}
