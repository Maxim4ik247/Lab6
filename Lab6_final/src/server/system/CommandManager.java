package server.system;


import data.network.Request;
import server.commands.*;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, BaseCommand> commandMap = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, WorkerCreator workerCreator) {
        commandMap.put("help", new HelpCommand(this));
        commandMap.put("info", new InfoCommand(collectionManager));
        commandMap.put("show", new ShowCommand(collectionManager));
        commandMap.put("add", new AddCommand(collectionManager));
        commandMap.put("update", new UpdateCommand(collectionManager, workerCreator));
        commandMap.put("remove_by_id", new RemoveByIdCommmand(collectionManager));
        commandMap.put("clear", new ClearCommand(collectionManager));
        commandMap.put("exit", new ExitCommand());
        commandMap.put("remove_first", new RemoveFirstCommand(collectionManager));
        commandMap.put("shuffle", new ShuffleCommand(collectionManager));
        commandMap.put("reorder", new ReorderCommand(collectionManager));
        commandMap.put("average_of_salary", new AverageOfSalaryCommand(collectionManager));
        commandMap.put("print_ascending", new PrintAscendingCommand(collectionManager));
        commandMap.put("print_field_descending_salary", new PrintFieldDescendingSalary(collectionManager));
        commandMap.put("execute_script", new ExecuteScriptCommand(collectionManager, new ScriptExecutor(this)));
        commandMap.put("save", new SaveDataCommand(collectionManager));
    }

    public String doCommand (Request input) {
        String commandName = input.getCommand().split(" ")[0];
        BaseCommand command = commandMap.get(commandName);
        System.out.println (commandName);
        if (command != null) {
            return command.executeCommand(input);
        } else {
            return "Неправильная команда: " + commandName;
        }
    }

    public String help() {
        for (BaseCommand c : commandMap.values()) {
            System.out.println(c.getCommandName() + " - " + c.getCommandDescription());
        }
        return "";
    }

}
