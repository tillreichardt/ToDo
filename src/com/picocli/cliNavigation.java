package com.picocli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "todo", subcommands = {
        ShowCommand.class,
        CreateCommand.class,
        UpdateCommand.class,
        DeleteCommand.class,
        CustomHelpCommand.class
}, description = "Manage todo items")
public class cliNavigation {

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new cliNavigation());
        // no arguments
        if (args.length == 0) {
            commandLine.usage(System.out);
            new CustomHelpCommand().printShowCommandOptions();
        } else {
            // execute command
            int exitCode = commandLine.execute(args);
            System.exit(exitCode);
        }
    }
}
