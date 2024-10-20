package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Scanner;

@Command(name = "show", description = "Show an entity base on type")
public class ShowCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to create: [user, todo, category]")
    String type;

    @Option(names = {"--sort-by", "-s"}, description = "Sort by field: [priority, date]")
    String sortBy;
    
    @Option(names = {"--order", "-o"}, description = "Sort order: [asc, desc]", defaultValue = "asc")
    String order;

    DatabaseConnector db = cliNavigation.getDatabaseConnector(); 
    
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        switch(type){
            case "user" -> {
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

            }
        }
        scanner.close();
    }
}
