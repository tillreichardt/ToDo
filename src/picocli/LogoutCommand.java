package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;

@Command(name = "logout", description = "Log out of your account")
public class LogoutCommand implements Runnable {
    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        if(db.getSessionID()==0){
            System.out.println("You are already logged out!");
            System.out.printf("Use the following command to log in: 'todo login -u [username] -p [password]' %nor create a new user using: 'todo create user -u [username] -p [password]'");
            return;
        }
        db.updateSessionID(0);
        System.out.println("You successfully logged out of your account!");
    }    
}
