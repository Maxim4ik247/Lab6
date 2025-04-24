package server;

import server.commands.SaveDataCommand;
import server.system.CollectionManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        CollectionManager collectionManager = new CollectionManager();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                SaveDataCommand save = new SaveDataCommand(collectionManager);
                save.executeCommand();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
        Server server = new Server(collectionManager);
        server.startServer("./data.xml");
    }
}