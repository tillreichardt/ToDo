package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Scanner;

@Command(name = "delete", description = "Delete an entity based on type")
public class DeleteCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to delete: [user, todo, category]", defaultValue = "")
    String type;

    @Option(names = {"--id", "-i"}, description = "ID of the item to delete")
    Integer id;
    
    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        if(db.getSessionID()==0){
            System.out.printf("You are not logged in!%nUse the following command to log in: 'todo login -u [username] -p [password]' %nor create a new user using: 'todo create user -u [username] -p [password]'");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        if (type.equals("")) {
            type = cliNavigation.getInputWithValidation(scanner, "Please specify the type [user, todo, category]: ", "^(user|todo|category)$");
        }
        switch(type){
            case "user" -> {
                if(db.getSessionID()!=1){ // user is not an admin and can only delete own account
                    String response = cliNavigation.getInputWithValidation(scanner,"Are you sure you want to delete your Account? (yes/no): ", "^(yes|no)$");
                    if(response.equals("no")){
                        System.out.println("User deletion canceled");
                        return;
                    }    
                    String confirmation = cliNavigation.getInputWithValidation(scanner,"Confirm your deletion with 'CONTINUE': ", "^(CONTINUE)$");
                    if(!(confirmation.equals("CONTINUE"))){
                        System.out.println("User deletion canceled");
                        return;
                    }
                    if(db.deleteUser(db.getSessionID())){
                        db.updateSessionID(0);
                        return;
                    } 
                } else if(id==null){ // user is admin and can delete anybody
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of user to delete: ", "^[0-9]+$"));
                    if(db.findUserByID(id)==0){
                        System.out.printf("User with ID '%d' was not found.%n", id);
                    } else {
                        db.deleteUser(id);
                    }
                }                
            }

            case "todo" -> {
                if (id == null) {
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of ToDo to delete: ", "^[0-9]+$"));
                }
                
                if (db.getSessionID() != 1) { // not an admin
                    int[] sharedToDos = db.getSharedToDosID(db.getSessionID(), "date", "asc");
                    int[] ownedToDos = db.getOwnedToDosID(db.getSessionID(), "date", "asc");
                    
                    // if todo is owned -> delete todo -> "todo is deleted from the database"
                    // if todo is shared and therefore not owned -> deleteSharedConnection -> "todo is only deleted from my account, not from the database"

                    // check if todo is owned
                    boolean isOwned = false;
                    for (int ownedId : ownedToDos) {
                        if (id == ownedId) {
                            isOwned = true;
                            break;
                        }
                    }
                    if (!isOwned) {
                        boolean isShared = false;
                        // check if todo is shared
                        for (int sharedId : sharedToDos) {
                            if (id == sharedId) {
                                isShared = true;
                                break;
                            }
                        }
                        if (!isShared) {
                            System.out.printf("ToDo with ID '%d' was not found.%n", id);
                            return;
                        }
                        db.deleteSharedConnection(db.getSessionID(), id);
                        System.out.printf("Connection to shared ToDo with ID '%d' has been removed.%n", id);
                        return;
                    }
                    db.deleteToDo(id);
                    System.out.printf("ToDo with ID '%d' has been deleted.%n", id);
                } else {
                    // delete todo when user is an admin, even if this is not his todo 
                    if (db.findToDoByID(id) == 0) {
                        System.out.printf("ToDo with ID '%d' was not found.%n", id);
                    } else {
                        db.deleteToDo(id);
                    }
                }
            }
            

            case "category" -> {
                if(id==null){
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of Category to delete: ", "^[0-9]+$"));
                }
                if(db.findCategoryByID(id)==0){
                    System.out.printf("Category with ID '%d' was not found.%n", id);
                } else {
                    db.deleteCategory(id);
                }
            }

            default -> {
                System.out.println("Unknown type: " + type);
            }
        }
        scanner.close();
    }
}
