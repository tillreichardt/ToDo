package com;

import com.Connector.DatabaseConnector;
import java.util.Scanner;
public class Navigation {

    private int userID = 0;
    private boolean loggedin = false;
    private boolean continueLoop = true;
    private DatabaseConnector db = new DatabaseConnector();

    public void navigationLoop(){
        Scanner scan = new Scanner(System.in);
        int response = 0;
        while (continueLoop) {
            if(loggedin){
                System.out.println("You can choose between 5 actions:"+
                            "\n1. log out"+
                            "\n2. show my ToDos"+
                            "\n3. create a new ToDo"+
                            "\n4. create a new category\n");
                
                response = Integer.parseInt(getInputWithValidation(scan, "What action do you want to do? Please enter a number between 1 and 4: ", "[1-4]"));
            } else {
                System.out.println("You can choose between 5 actions:"+
                            "\n\n5. log in"+
                            "\n6. create a new user");
                response = Integer.parseInt(getInputWithValidation(scan, "What action do you want to do? Please enter a number between 5 and 6: ", "[5-6]"));
            }
            switch(response){
                case 1 -> {
                    loggedin = false;
                    userID = 0;
                }

                case 2 -> {
                    
                }

                case 5 -> {
                    String username = getInputWithValidation(scan, "Please enter your username: ", "\\S+");
                    String password = getInputWithValidation(scan, "Please enter your password: ", "\\S+");
                    checkLogin(password, username);
                    userID = db.findUserByName(username);
                    loggedin = true;    
                }
            }
        }
        scan.close();
    }

    public boolean checkLogin(String password, String username){
        if(db.findUserByName(username)==0){
            //user not found
            return false;
        }
        if(!(db.getPassword(db.findUserByName(username)).equals(password))){
            System.out.println("The entered password is wrong!");
            return false;
        }
        System.out.println("Log in was successful.");
        return true;
    }

    private String getInputWithValidation(Scanner scanner, String question, String regex) {
        // getInputWithValidation(scan, "Mit wie vielen Spielern möchtest du spielen? (max. 8): ", "[1-8]")
        // get user input until he enters something that matches the regex
        String input;
        do {
            System.out.print(question);
            input = scanner.nextLine().trim();
            if (!input.matches(regex)) {
                System.out.println("Invalid Input. Please try again!");
            }
        } while (!input.matches(regex));

        return input;
    }
}
