package all;

import cliPack.MenuHandler;
import domainLogic.Manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ServerMenuHandler implements MenuHandler {
    private Manager manager;
    private JosCommands josCommands;

    public ServerMenuHandler(Manager manager, JosCommands josCommands) {
        this.manager = manager;
        this.josCommands = josCommands;
    }

    @Override
    public void create() {
        manager.create();
    }

    @Override
    public List<String> read() {
        return manager.read();
    }

    @Override
    public void update(String address, long newAccessCount) {
        manager.update(address, newAccessCount);
    }

    @Override
    public void delete(String address) {
        manager.delete(address);
    }

    @Override
    public void save() throws IOException {
        josCommands.saveState(manager);
    }

    @Override
    public void load() throws IOException, ClassNotFoundException {
        manager = josCommands.loadState();
    }

    @Override
    public void logout() {
        manager.logout();
    }

    public String handleCommand(String cmd, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        switch (cmd) {
            case "create":
                create();
                return "Audio created.";
            case "read":
                return read().toString();
            case "update":
                String address = (String) ois.readObject();
                long newAccessCount = ois.readLong();
                update(address, newAccessCount);
                return "Audio updated.";
            case "delete":
                address = (String) ois.readObject();
                delete(address);
                return "Audio deleted.";
            case "save":
                save();
                return "State saved.";
            case "load":
                load();
                return "State loaded.";
            case "logout":
                logout();
                return "Logged out.";
            default:
                return "Unknown command.";
        }
    }
}
