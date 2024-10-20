package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Scanner;

@Command(name = "show", description = "Show an entity base on type")
public class ShowCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to create: [user, todo, category]", defaultValue = "")
    String type;

    @Option(names = {"--sort-by", "-s"}, description = "Sort items by: [priority, date]")
    String sortBy;
    
    @Option(names = {"--order", "-o"}, description = "Order of sorting: [asc, desc]", defaultValue = "asc")
    String order;

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
                if(db.getSessionID()!=1){
                    System.out.println("Only admins can see all users!");
                    return;
                }
                System.out.printf("Showing users...%n");
                for(int i = 0; i < db.getUser().length;i++){
                    System.out.println("["+db.getUserID()[i]+"] " + db.getUser()[i]);
                }
            }

            case "todo" -> {
                if (sortBy == null) {
                    sortBy = cliNavigation.getInputWithValidation(scanner, "Please enter sort by field [priority, date]: ", "^(date|priority)$");
                }
                System.out.printf("Showing todo items sorted by %s in %s order...%n", sortBy, order);
                for(int i = 0; i < (db.getToDos(db.getSessionID(),sortBy,order).length);i++){
                    System.out.println("["+db.getToDosID(db.getSessionID(),sortBy,order)[i]+"] " + db.getToDos(db.getSessionID(),sortBy,order)[i]);
                }
            }

            case "category" -> {
                System.out.printf("Showing categories...%n");
                for(int i = 0; i < db.getCategories().length;i++){
                    System.out.println("["+db.getCategoriesID()[i]+"] " + db.getCategories()[i]);
                }
            }

            default -> {
                System.out.println("Unknown type: " + type);
            }
        }
        scanner.close();
    }
}
