package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;

@Command(name = "logout", description = "Log out of your account")
public class LogoutCommand implements Runnable {
    DatabaseConnector db = cliNavigation.getDatabaseConnector();
    
    @Override
    public void run() {
       db.updateSessionID(0);
       System.out.println("You successfully logged out of your accout!");
    }    
}
