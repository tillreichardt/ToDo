package picocli;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Scanner;

import com.PasswordHasher;
import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;

@Command(name = "update", description = "Update an existing entity based on type")
public class UpdateCommand implements Runnable{
  	
	@Parameters(index = "0", description = "Type of item to create: [user, todo, category]", defaultValue = "")
    String type;

    @Option(names = {"--username", "-u"}, description = "New username (max. 32 characters)")
    String username;

    @Option(names = {"--password", "-p"}, description = "New password for the user")
    String password;

    @Option(names = {"--title", "-t"}, description = "New title of the ToDo or category")
    String title;
    
    @Option(names = {"--description", "-d"}, description = "Updated description of the ToDo")
    String description;

    @Option(names = {"--priority", "-pr"}, description = "Updated priority: [0 = high, 1 = low]")
    Integer priority;

    @Option(names = {"--category", "-c"}, description = "New category for the ToDo")
    String category;

	@Option(names = {"--id", "-i"}, description = "ID of the item to update")
    Integer id;

	DatabaseConnector db = cliNavigation.getDatabaseConnector();

  	@Override
	public void run() {
		if(db.getSessionID()==0){
            System.out.printf("You are not logged in!%nUse the following command to log in: 'todo login -u [username] -p [password]' %nor create a new user using: 'todo create user -u [username] -p [password]'");
            return;
        }
		
		PasswordHasher ph = new PasswordHasher();
		Scanner scanner = new Scanner(System.in);

		if (type.equals("")) {
			type = cliNavigation.getInputWithValidation(scanner, "Please specify the type [user, todo, category]: ", "^(user|todo|category)$");
		}
		switch(type) {
			case "user" -> {
				if(db.getSessionID()==1){
					System.out.println("As an admin, you cannot change your username and password nor other user's names and passwords");
					return;
				}
				String response = cliNavigation.getInputWithValidation(scanner, "Do you want to update your username or password?: ", "^(username|password)$");
                if(response.equals("username")){
					if(username == null){
						username = cliNavigation.getInputWithValidation(scanner, "Please enter your new username: ", "^.*$");
					}
					if(db.findUserByName(username)!=0){
						System.out.printf("User with the username: '%s' already exists", username);
						return;
					}
					db.updateUserName(username, db.getSessionID());
					System.out.printf("Your username has been updated to '%s'! ", username);
				} else {
					if (password == null) {
						password = cliNavigation.getInputWithValidation(scanner, "Please enter your new password: ", "^.*$");
					}
					db.updateUserPassword(ph.hashPassword(password), db.getSessionID());
					System.out.printf("Your password has been updated!");
				}               
			}
			case "todo" -> {
				if(id==null){
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of the ToDo to update: ", "^[0-9]+$"));
                }
				
                if(db.getSessionID()!=1){ // not an admin
                    int[] toDosOfUser = db.getToDosID(db.getSessionID(), "date", "asc");
                    boolean found = false;
                    for(int i = 0; i < toDosOfUser.length; i++){
                        if(id == toDosOfUser[i]) found = true;
                    }
                    if(!found){
                        System.out.printf("ToDo with ID '%d' was not found.%n", id);
                        return;
                    }
                }
                
                if(db.findToDoByID(id)==0){
                    System.out.printf("ToDo with ID '%d' was not found.%n", id);
                } else {
                    String response = cliNavigation.getInputWithValidation(scanner, "What do you want to update? [title, description, priority, category]: ", "^(title|description|priority|category)$");
                	switch(response){
						case "title" -> {
							if(title == null){
								title = cliNavigation.getInputWithValidation(scanner, "Please enter new ToDo title: ", "^.{1,128}$");
							}
							db.updateToDoTitle(title, id);
							System.out.printf("The title of Todo with ID '%d' has been updated to '%s'.%n", id, title);
						}
						case "description" -> {
							if(description == null){
								description = cliNavigation.getInputWithValidation(scanner, "Please enter new description for the ToDo: ", "^.{1,1024}$");
							}
							db.updateToDoDescription(description, id);
							System.out.printf("The description of Todo with ID '%d' has been updated to '%s'.%n", id, description);
						}
						case "priority" -> {
							if(priority == null || (priority != 0 && priority != 1)){
								priority = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter new ToDo priority [0, 1]: ", "[0-1]"));
							}
							db.updateToDoPriority(priority, id);
							System.out.printf("The priority of Todo with ID '%d' has been updated to '%d'.%n", id, priority);
						}
						case "category" -> {
							if(category == null){
								category = cliNavigation.getInputWithValidation(scanner, "Please enter new category title: ", "^.{1,128}$");
							}
							if(db.findCategoryByDescription(category)==0){
								System.out.printf("Category with title '%s' was not found.%n", category);
								return;
							}
							db.updateToDoCategory(db.findCategoryByDescription(category), id);
							System.out.printf("The category of Todo with ID '%d' has been updated to '%s'.%n", id, category);
						}
				}
                }
			}
			case "category" -> {
				if(id==null){
                    id = Integer.parseInt(cliNavigation.getInputWithValidation(scanner, "Please enter ID of the category to update: ", "^[0-9]+$"));
                }
				if(db.findCategoryByID(id)==0){
					System.out.printf("Category with ID '%d' was not found.%n", id);
					return;
				}
				if(id==1){
					System.out.println("Default category cannot be updated!");
					return;
				}
				if(title == null){
					title = cliNavigation.getInputWithValidation(scanner, "Please enter new title for the category: ", "^.{1,32}$");
				}
				db.updateCategory(title, id);
				System.out.printf("The title of Category with ID '%d' has been updated to '%s'.%n", id, title);
			}
			default -> {
				System.out.println("Unknown type: " + type);
			}
		}
		scanner.close();
	}
}
