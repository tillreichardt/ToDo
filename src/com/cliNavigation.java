package com;

import com.Connector.DatabaseConnector;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.Scanner;

@Command(
    name = "todo", 
    descriptionHeading = "\nType of items available for all commands: [user, todo, category]\n\n",
    customSynopsis = "todo [COMMAND] [TYPE] [OPTIONS]",
    subcommands = {
        picocli.ShowCommand.class, 
        picocli.CreateCommand.class, 
        picocli.DeleteCommand.class,
        picocli.UpdateCommand.class,
        picocli.LoginCommand.class,
        picocli.LogoutCommand.class,
        picocli.CustomHelpCommand.class
    }
)
public class cliNavigation {

    private static DatabaseConnector db;

    public cliNavigation(){
        db = new DatabaseConnector();
    }
    
    public static DatabaseConnector getDatabaseConnector(){
        return db;
    }

    public static String getInputWithValidation(Scanner scanner, String question, String regex) {
        // get user input until he enters something that matches the regex
        String input;
        do {
            System.out.print(question);
            input = scanner.nextLine().trim();
            if (!input.matches(regex)) {
                System.out.println("Invalid Input. Please try again!");
            }
        } while (!input.matches(regex));
        return input;
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new cliNavigation());
        if (args.length == 0) {
            if(db.getSessionID()==0){
                System.out.printf("You are not logged in!%nUse the following command to log in: 'todo login -u [username] -p [password]' %nor create a new user using: 'todo create user -u [username] -p [password]'");
                return;
            }
            commandLine.usage(System.out);
            new picocli.CustomHelpCommand().printCommandOptions();
        } else {
            int exitCode = commandLine.execute(args);
            System.exit(exitCode);
        }
    }
}
