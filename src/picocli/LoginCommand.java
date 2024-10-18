package picocli;

import com.PasswordHasher;
import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "login", description = "Log in to your account")
public class LoginCommand implements Runnable {
    @Option(names = {"--username", "-u"}, description = "Enter username of your account")
    String username;

    @Option(names = {"--password", "-p"}, description = "Enter password of your account")
    String password;

    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        if(checkLogin(password, username)){
            System.out.println("Login was successful!");
        } else {
            System.out.println("Login was unsuccessful. Please try again!");
        }
    }

    
    private boolean checkLogin(String password, String username){
        PasswordHasher ph = new PasswordHasher();
        String hashedPassword = ph.hashPassword(password);
        int userID = db.findUserByName(username);

        if(userID==0){
            System.out.println("A user '"+username+"' was not found!");
            return false;
        }
        if(!(db.getPassword(userID).equals(hashedPassword))){
            System.out.println("Wrong password!");
            return false;
        }
        // login successful
        cliNavigation.setUserID(userID);
        return true;
    }
    
}
