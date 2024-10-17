package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete an entity based on type")
public class DeleteCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to delete: [user, todo]")
    String type;

    @Option(names = "--name", description = "The name of the user to delete")
    String userName;

    @Option(names = "--id", description = "The ID of the todo item to delete")
    Integer todoId;

    @Override
    public void run() {
        DatabaseConnector dbConnector = cliNavigation.getDatabaseConnector();
        if ("user".equalsIgnoreCase(type)) {
            if (userName != null) {
                System.out.printf("Deleting user account with name: %s%n", userName);
                
            } else {
                System.err.println("Error: Please specify a name for the user using --name");
            }
        } else if ("todo".equalsIgnoreCase(type)) {
            if (todoId != null) {
                System.out.printf("Deleting todo item with ID: %d%n", todoId);
                
            } else {
                System.err.println("Error: Please specify an ID for the todo using --id");
            }
        } else {
            System.err.println("Error: Invalid type. Use 'user' or 'todo'.");
        }
    }
}
