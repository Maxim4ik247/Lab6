package server.commands;

import data.Worker;
import data.network.Request;
import server.system.CollectionManager;
import server.util.WriteToFile;

public class SaveDataCommand {

    private final CollectionManager collectionManager;

    public SaveDataCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String executeCommand() {
        String data = "<workers>";
        for (Worker w : collectionManager.getworkerLinkedList()) {
            data += "\n\t" + w.toXML();
        }
        data += "\n</workers>";
        WriteToFile.writeToFile(data);
        return "файл сохранен";
    }
}