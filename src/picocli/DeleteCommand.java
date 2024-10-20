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

    @Option(names = {"--id", "-i"}, description = "The ID of the item to delete")
    Integer id;
    
    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        if (type.equals("")) {
            type = cliNavigation.getInputWithValidation(scanner, "Please specify the type [user, todo, category]: ", "^(user|todo|category)$");
        }
        switch(type){
            case "user" -> {
                if(id==null){
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of user to delete: ", "^[0-9]+$"));
                }
                if(db.findUserByID(id)==0){
                    System.out.printf("User with ID '%d' was not found.%n", id);
                } else {
                    db.deleteUser(id);
                }
            }

            case "todo" -> {
                if(id==null){
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of ToDo to delete: ", "^[0-9]+$"));
                }
                if(db.findToDoByID(id)==0){
                    System.out.printf("ToDo with ID '%d' was not found.%n", id);
                } else {
                    db.deleteToDo(id);
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
