package picocli;

import com.PasswordHasher;
import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create a new entity based on type")
public class CreateCommand implements Runnable {

    @Parameters(index = "0", description = "Type of item to create: [user, todo, category]")
    String type;

    @Option(names = {"--username", "-u"}, description = "The name of the user to create")
    String username;

    @Option(names = {"--password", "-p"}, description = "The password of the user to create")
    String password;

    @Option(names = {"--title", "-t"}, description = "The title of ToDo or category to create")
    String title;
    
    @Option(names = {"--description", "-d"}, description = "Description of ToDo", defaultValue = "")
    String description;

    @Option(names = {"--importance", "-i"}, description = "Importance of ToDo", defaultValue = "0")
    String importance;

    @Option(names = {"--category", "-c"}, description = "Category of ToDo", defaultValue = "0")
    String category;

    

    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        PasswordHasher ph = new PasswordHasher();

        switch(type){
            case "user" -> {
                if (username == null) {
                    System.out.println("Error: Please specify a name for the user using --name or -n");
                    return;   
                }
                if(db.findUserByName(username)!=0){
                    System.out.println("Error: User with the username: '"+ username +"' already exists");
                    return;
                }
                db.addUser(username, ph.hashPassword(password));
                System.out.printf("Creating user account with name: %s%n", username);
            }
            case "todo" -> {
                if (title == null) {
                    System.out.println("Error: Please specify a title for the todo using --title or -t");
                    return;   
                }
                db.createToDo(description, title, Integer.parseInt(importance), Integer.parseInt(category), cliNavigation.getUserID());
            }
            case "category" -> {
                if (title != null) {
                    System.out.printf("Creating category item with title: %s%n", title);
                } else {
                    System.err.println("Error: Please specify a title for the category using --title or -t");
                }
            }
        }
    }
}
