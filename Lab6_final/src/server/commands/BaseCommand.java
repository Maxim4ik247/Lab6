package server.commands;

import data.network.Request;

public interface BaseCommand {
    String executeCommand(Request input);

    String getCommandName();

    String getCommandDescription();
}
