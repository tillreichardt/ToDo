package picocli;

import com.PasswordHasher;
import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Scanner;

// checken ob UserID in Session= 0 ist, dann log out, sonst ist jemand eingeloggt. 
// Beim ausloggen auf 0 setzen, beim einloggen auf aktuelle Userid

@Command(name = "login", description = "Log in to your account")
public class LoginCommand implements Runnable {
    @Option(names = {"--username", "-u"}, description = "Enter username of your account")
    String username;

    @Option(names = {"--password", "-p"}, description = "Enter password of your account")
    String password;

    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        if(db.getSessionID()!=0){
            System.out.println("You are already logged in!");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        if(username==null){
            System.out.print("Please enter your username: ");
            username = scanner.nextLine();
        }
        if(password==null){
            System.out.print("Please enter your password: ");
            password = scanner.nextLine();
        }
        if(checkLogin(password, username)){
            System.out.println("Login was successful!");
            db.updateSessionID(db.findUserByName(username));
        } else {
            System.out.println("Login was unsuccessful. Please try again!");
        }
        scanner.close();
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
        return true;
    }
    
}
