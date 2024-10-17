package picocli;

import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "login", description = "Log in to your account")
public class LoginCommand implements Runnable {
    @Option(names = {"--username", "-u"}, description = "Enter username of your account")
    String username;

    @Option(names = {"--password", "-p"}, description = "Enter password of your account")
    String password;

    

    @Override
    public void run() {
        /*if(checkLogin(password, username)){
            System.out.println("Login was successful...");
        } else {
            System.out.println("Login unsuccessful. Please try again...");
        }*/
        DatabaseConnector db = new DatabaseConnector(); 
        System.out.println("username: "+ username);
        System.out.println("password: "+ password);
        db.connect();
        db.test();
    }

    /*
    private boolean checkLogin(String password, String username){
        
        if(db.findUserByName(username)==0){
            //user not found
            return false;
        }
        if(!(db.getPassword(db.findUserByName(username)).equals(password))){
            // wrong password
            return false;
        }
        return true;
    }
    */
}
