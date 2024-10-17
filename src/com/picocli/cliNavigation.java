package com.picocli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "todo", subcommands = {
        ShowCommand.class,
        CreateCommand.class,
        UpdateCommand.class,
        DeleteCommand.class,
        CommandLine.HelpCommand.class
}, description = "Manage todo items")
public class cliNavigation {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new cliNavigation()).execute(args);
        System.exit(exitCode);
    }
}
