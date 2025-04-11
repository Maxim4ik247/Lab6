package server.commands;


import data.network.Request;
import server.system.CollectionManager;
import server.system.WorkerCreator;

public class UpdateCommand implements BaseCommand {


    private CollectionManager collectionManager;
    private WorkerCreator workerCreator;

    public UpdateCommand(CollectionManager collectionManager, WorkerCreator workerCreator) {
        this.collectionManager = collectionManager;
        this.workerCreator = workerCreator;
    }

    @Override
    public String executeCommand(Request i) {
        int id = Integer.parseInt(i.getCommand().split(" ")[1]);
        return workerCreator.updateWorker(id, collectionManager, i.getWorker()).toString();
    }

    @Override
    public String getCommandName() {
        return "update";
    }

    @Override
    public String getCommandDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
