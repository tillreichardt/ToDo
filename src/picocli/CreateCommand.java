package picocli;

import com.PasswordHasher;
import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Scanner;

@Command(name = "create", description = "Create a new entity based on type")
public class CreateCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to create: [user, todo, category]", defaultValue = "")
    String type;

    @Option(names = {"--username", "-u"}, description = "Username (max. 32 characters)")
    String username;

    @Option(names = {"--password", "-p"}, description = "User's password")
    String password;

    @Option(names = {"--title", "-t"}, description = "Title (max. 128 characters for todos, 32 for categories)")
    String title;
    
    @Option(names = {"--description", "-d"}, description = "Detailed description of the ToDo", defaultValue = "")
    String description;

    @Option(names = {"--priority", "-pr"}, description = "Priority level: [0 = high, 1 = low]", defaultValue = "1")
    Integer priority;

    @Option(names = {"--category", "-c"}, description = "Associated category", defaultValue = "default")
    String category;

    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    // command
    @Override
    public void run() {
        PasswordHasher ph = new PasswordHasher();
        Scanner scanner = new Scanner(System.in);

        if (type.equals("")) {
            type = cliNavigation.getInputWithValidation(scanner, "Please specify the type [user, todo, category]: ", "^(user|todo|category)$");
        }
        switch(type) {
            case "user" -> {
                if (username == null) {
                    username = cliNavigation.getInputWithValidation(scanner, "Please enter the username: ", "^.{1,32}$");
                }

                if (db.findUserByName(username) != 0) {
                    System.out.printf("User with the username: '%s' already exists!%n", username);
                    scanner.close();
                    return;
                }

                if (password == null) {
                    password = cliNavigation.getInputWithValidation(scanner, "Please enter the password: ", "^.*$");
                }

                db.addUser(username, ph.hashPassword(password));
                System.out.printf("User '%s' has been successfully created.%n", username);
            }
            case "todo" -> {
                if(db.getSessionID()==0){
                    System.out.printf("You are not logged in!%nUse the following command to log in: 'todo login -u [username] -p [password]' %nor create a new user using: 'todo create user -u [username] -p [password]'");
                    return;
                }
                if(title == null) {
                    title = cliNavigation.getInputWithValidation(scanner, "Please enter the title for the ToDo: ", "^.{1,128}$");
                }
                if(db.findCategoryByDescription(category) == 0){
                    System.out.printf("Category with title '%s' was not found.%n", category);
                    return;
                }
                if(priority != 0 && priority != 1){
                    System.out.println("Use 0 for a high priority and 1 for a low priority");
                    return;
                }
                db.createToDo(description, title, priority, db.findCategoryByDescription(category), db.getSessionID());
                System.out.printf("ToDo '%s' has been successfully created.%n", title);
            }
            case "category" -> {
                if(db.getSessionID()==0){
                    System.out.printf("You are not logged in!%nUse the following command to log in: 'todo login -u [username] -p [password]' %nor create a new user using: 'todo create user -u [username] -p [password]'");
                    return;
                }
                if (title == null) {
                    title = cliNavigation.getInputWithValidation(scanner, "Please enter the title for the category: ", "^.{1,128}$");
                }
                if(db.findCategoryByDescription(title)!= 0){
                    System.out.printf("Category with the title '%s' already exists!%n", title);
                    return;
                }
                db.createCategory(title);
                System.out.printf("Category '%s' has been successfully created.%n", title);
            }
            default -> {
                System.out.println("Unknown type: " + type);
            }
        }
        scanner.close();
    }
}
