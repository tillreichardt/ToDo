package picocli;

import java.util.Scanner;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


@Command(name = "share", description = "Share your Information")
public class ShareCommand implements Runnable {
    @Parameters(index = "0", description = "Type of item to create: [user, todo, category]", defaultValue = "")
    String type;

    @Option(names = {"--todoid", "-id"}, description = "ID of todo you want to share")
    Integer todoID;

    @Option(names = {"--publicid", "-pID"}, description = "ID of user you want to share a ToDo with")
    String publicID;

    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        if (type.equals("")) {
			type = cliNavigation.getInputWithValidation(scanner, "Please specify the type [user, todo, category]: ", "^(user|todo|category)$");
		}
		switch(type) {
            case "user" -> {
                System.out.printf("You cannot share an user!%n");
            }
            case "todo" -> {
                if(todoID == null){
					todoID = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of the ToDo you want to share: ", "^.{1,32}$"));
				}
                int[] ownedToDos = db.getOwnedToDosID(db.getSessionID(), "date", "asc");
                // check if todo is owned
                boolean isOwned = false;
                for (int ownedId : ownedToDos) {
                    if (todoID == ownedId) {
                        isOwned = true;
                        break;
                    }
                }
                if (!isOwned) {
                    System.out.printf("You can only share your own ToDos!%n");        
                    return;
                }

                // get publicID
                if(publicID == null){
                    publicID = cliNavigation.getInputWithValidation(scanner, "Please enter publicID of the user you want to share your ToDo with: ", "^.{1,32}$");
                }

                // check if publicID is usable
                if(db.getUserIDFromPublicID(publicID) == 0){
                    System.out.printf("User with publicID '%s' was not found.%n%nYou can use the following command get your own publicID: 'todo share user'%n", publicID);
                    return;
                } else if(db.getUserIDFromPublicID(publicID) == db.getSessionID()){
                    System.out.printf("You cannot share a ToDo with yourself!%n");
                    return;
                }
                db.setSharedConnection(db.getUserIDFromPublicID(publicID), todoID);
                System.out.printf("ToDo with ID '%d' has been shared with User '%s'.%n", todoID, publicID);
            }
            case "category" -> {
                System.out.printf("You cannot share a category!%n");
            }

        }
        scanner.close();
    }
}
