package picocli;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;

@Command(name = "update", description = "Update an existing entity based on type")
public class UpdateCommand implements Runnable{
  	@Parameters(index = "0", description = "The ID of the todo item to update")
  	int id;

  	@Option(names = "--title", description = "New title for the todo item")
   	String title;

	DatabaseConnector db = cliNavigation.getDatabaseConnector();

  	@Override
	public void run() {
		if(db.getSessionID()==0){
            System.out.println("Use the login command to login");
            return;
        }
		System.out.printf("Updating todo item %d with new title: %s%n", id, title);
	}
}
