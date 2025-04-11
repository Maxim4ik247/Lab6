package server.commands;

import data.Worker;
import data.network.Request;
import server.system.CollectionManager;
import server.util.WriteToFile;

public class SaveDataCommand implements BaseCommand {

    private final CollectionManager collectionManager;

    public SaveDataCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeCommand(Request i) {
        String data = "<workers>";
        for (Worker w : collectionManager.getworkerLinkedList()) {
            data += "\n\t" + w.toXML();
        }
        data += "\n</workers>";
        WriteToFile.writeToFile(data);
        return "файл сохранен";
    }

    @Override
    public String getCommandName() {
        return "save";
    }

    @Override
    public String getCommandDescription() {
        return "сохранить коллекцию в файл";
    }
}
