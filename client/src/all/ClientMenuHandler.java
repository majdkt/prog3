package all;


import cliPack.MenuHandler;

import java.io.IOException;
import java.util.List;

public class ClientMenuHandler implements MenuHandler {
    private ServerInterface serverInterface;

    public ClientMenuHandler(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    @Override
    public void create() throws IOException, ClassNotFoundException {
        serverInterface.sendCommand("CREATE");
    }

    @Override
    public List<String> read() throws IOException, ClassNotFoundException {
        return serverInterface.sendReadCommand("READ");
    }

    @Override
    public void update(String address, long newAccessCount) throws IOException, ClassNotFoundException {
        serverInterface.sendCommand("UPDATE");
        serverInterface.sendObject(address);
        serverInterface.sendObject(newAccessCount);
    }

    @Override
    public void delete(String address) throws IOException, ClassNotFoundException {
        serverInterface.sendCommand("DELETE");
        serverInterface.sendObject(address);
    }

    @Override
    public void save() throws IOException {
        try {
            serverInterface.sendCommand("SAVE");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() throws IOException, ClassNotFoundException {
        serverInterface.sendCommand("LOAD");
    }

    @Override
    public void logout() throws IOException {
        try {
            serverInterface.sendCommand("LOGOUT");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
