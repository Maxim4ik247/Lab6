package server.commands;


import data.network.Request;
import server.system.CollectionManager;

public class AverageOfSalaryCommand implements BaseCommand {
    private final CollectionManager collectionManager;

    public AverageOfSalaryCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeCommand(Request i) {
        return "Средняя зарплата всех работников:" + collectionManager.averageSalary();
    }

    @Override
    public String getCommandName() {
        return "average_of_salary";
    }

    @Override
    public String getCommandDescription() {
        return "вывести среднее значение поля salary для всех элементов коллекции";
    }
}
