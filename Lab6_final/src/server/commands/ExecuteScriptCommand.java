package server.commands;


import data.network.Request;
import server.system.CollectionManager;
import server.system.ScriptExecutor;

public class ExecuteScriptCommand implements BaseCommand {
    private final CollectionManager collectionManager;
    private final ScriptExecutor scriptExecutor;

    public ExecuteScriptCommand(CollectionManager collectionManager, ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeCommand(Request i) {
        return scriptExecutor.readFile(i.getCommand().split(" ")[1], collectionManager);
    }

    @Override
    public String getCommandName() {
        return "execute_script";
    }

    @Override
    public String getCommandDescription() {
        return "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
