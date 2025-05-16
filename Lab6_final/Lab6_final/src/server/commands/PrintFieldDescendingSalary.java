package server.commands;


import data.network.Request;
import server.system.CollectionManager;

public class PrintFieldDescendingSalary implements BaseCommand {

    private final CollectionManager collectionManager;

    public PrintFieldDescendingSalary(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeCommand(Request i) {
        collectionManager.getworkerLinkedList().stream().sorted().forEach(worker -> {
            System.out.println(worker.getSalary());
        });
        return "";
    }

    @Override
    public String getCommandName() {
        return "print_field_descending_salary";
    }

    @Override
    public String getCommandDescription() {
        return "вывести значения поля salary всех элементов в порядке убывания";
    }
}
