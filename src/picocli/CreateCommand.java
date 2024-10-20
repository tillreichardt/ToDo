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

    @Option(names = {"--username", "-u"}, description = "max. 32 character")
    String username;

    @Option(names = {"--password", "-pa"}, description = "The password of the user to create")
    String password;

    @Option(names = {"--title", "-t"}, description = "max. 32 char category / 128 todo")
    String title;
    
    @Option(names = {"--description", "-d"}, description = "Description of ToDo", defaultValue = "")
    String description;

    @Option(names = {"--priority", "-pr"}, description = "Priority of ToDo: [0, 1]", defaultValue = "1")
    Integer priority;

    @Option(names = {"--category", "-c"}, description = "Category of ToDo", defaultValue = "default")
    String category;

    DatabaseConnector db = cliNavigation.getDatabaseConnector();

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
                    System.out.println("User with the username: '" + username + "' already exists");
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
                if (title == null) {
                    title = cliNavigation.getInputWithValidation(scanner, "Please enter the title for the ToDo: ", "^.{1,128}$");
                }
                db.createToDo(description, title, priority, db.findCategoryByDescription(category), db.getSessionID());
                System.out.printf("ToDo '%s' has been successfully created.%n", title);
            }
            case "category" -> {
                if (title == null) {
                    title = cliNavigation.getInputWithValidation(scanner, "Please enter the title for the category: ", "^.{1,128}$");
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
