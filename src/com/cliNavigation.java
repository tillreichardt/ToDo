package com;

import com.Connector.DatabaseConnector;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "todo", 
    description = "ToDo management app",
    descriptionHeading = "\nType of items available for all commands: [user, todo, category]\n\n",
    customSynopsis = "todo [COMMAND] [TYPE] [OPTIONS]",
    subcommands = {
        picocli.ShowCommand.class, 
        picocli.CreateCommand.class, 
        picocli.DeleteCommand.class,
        picocli.UpdateCommand.class,
        picocli.LoginCommand.class,
        picocli.CustomHelpCommand.class
    }
)
public class cliNavigation {

    private static DatabaseConnector dbConn;

    public cliNavigation(){
        
        dbConn = new DatabaseConnector();
        //dbConn.test();
        userID = 0;
    }
    
    public static DatabaseConnector getDatabaseConnector(){
        return dbConn;
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new cliNavigation());
        //dbConn.updateSessionID(10);
        if (args.length == 0) {
            commandLine.usage(System.out);
            new picocli.CustomHelpCommand().printShowCommandOptions();
        } else {
            int exitCode = commandLine.execute(args);
            System.exit(exitCode);
        }
    }
}
