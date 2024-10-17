package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create a new entity based on type")
public class CreateCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to create: [user, todo]")
    String type;

    @Option(names = "--name", description = "The name of the user to create")
    String userName;

    @Option(names = "--title", description = "The title of the todo item to create")
    String todoTitle;
    
    @Override
    public void run() {
        DatabaseConnector dbConnector = cliNavigation.getDatabaseConnector();
        if ("user".equalsIgnoreCase(type)) {
            if (userName != null) {
                System.out.printf("Creating user account with name: %s%n", userName);
            
            } else {
                System.err.println("Error: Please specify a name for the user using --name");
            }
        } else if ("todo".equalsIgnoreCase(type)) {
            if (todoTitle != null) {
                System.out.printf("Creating todo item with title: %s%n", todoTitle);
                
            } else {
                System.err.println("Error: Please specify a title for the todo using --title");
            }
        } else {
            System.err.println("Error: Invalid type. Use 'user' or 'todo'.");
        }
    }
}
