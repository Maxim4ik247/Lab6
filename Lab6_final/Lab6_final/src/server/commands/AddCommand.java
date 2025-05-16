package server.commands;

import data.network.Request;
import server.system.CollectionManager;
import server.system.WorkerCreator;


public class AddCommand implements BaseCommand {
    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeCommand(Request i) {
        collectionManager.add(i.getWorker());
        return "Работник добавлен";
    }

    @Override
    public String getCommandName() {
        return "add";
    }

    @Override
    public String getCommandDescription() {
        return "добавить новый элемент в коллекцию";
    }
}
