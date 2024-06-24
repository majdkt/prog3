package cliPack;

import java.io.IOException;
import java.util.List;

public interface MenuHandler {
    void create() throws IOException, ClassNotFoundException;
    List<String> read() throws IOException, ClassNotFoundException;
    void update(String address, long newAccessCount) throws IOException, ClassNotFoundException;
    void delete(String address) throws IOException, ClassNotFoundException;
    void save() throws IOException;
    void load() throws IOException, ClassNotFoundException;
    void logout() throws IOException;
}
