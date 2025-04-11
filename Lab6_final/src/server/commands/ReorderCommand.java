package server.commands;


import data.network.Request;
import server.system.CollectionManager;

public class ReorderCommand implements BaseCommand {

    private final CollectionManager collectionManager;

    public ReorderCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeCommand(Request i) {
        collectionManager.reorder();
        return "Элементы коллекции отсортированы в обратном порядке";
    }

    @Override
    public String getCommandName() {
        return "reorder";
    }

    @Override
    public String getCommandDescription() {
        return "отсортировать коллекцию в порядке, обратном нынешнему";
    }
}
