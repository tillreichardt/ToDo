package com.picocli;

import com.PasswordHasher;
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
                System.out.println("\nYou can choose between 8 actions:"+
                            "\n1. log out"+
                            "\n2. show my ToDos"+
                            "\n3. create a new ToDo"+
                            "\n4. delete a ToDo"+
                            "\n5. create a new category"+
                            "\n6. delete a category"+
                            "\n7. delete user"+
                            "\n8. end programm");

                response = Integer.parseInt(getInputWithValidation(scan, "\nWhat action do you want to do? Please enter a number between 1 and 8: ", "[1-8]"));
            } else {
                System.out.println("\nYou can choose between 2 actions:"+
                            "\n\n9. log in"+
                            "\n10. create a new user");
                response = Integer.parseInt(getInputWithValidation(scan, "\nWhat action do you want to do? Please enter a number between 9 and 10: ", "^(9|10)$"));
            }
            switch(response){
                // log out
                case 1 -> {
                    loggedin = false;
                    userID = 0;
                    continueLoop = true;
                }

                // show todos
                case 2 -> {
                    if(userID==0){
                        System.out.println("You are not logged in");
                        break;
                    } 
                    if(db.getSharedConnections(userID).length == 0){
                        System.out.println("\nYou dont have any ToDos yet. Press 3 to create a new ToDo.");
                        continueLoop = true;
                        break;
                    }
                    System.out.println("Here are all of your ToDos:");
                    for(int i = 0; i < (db.getToDos(userID).length);i++){
                        System.out.println(db.getToDos(userID)[i]);
                    }
                }

                // create todo
                case 3 -> {
                    if(userID==0){
                        System.out.println("You are not logged in");
                        break;
                    } 
                    String title = getInputWithValidation(scan, "Please enter the title of the ToDo. It can have a length up to 128 characters: ", "^.{1,128}$");
                    String description = getInputWithValidation(scan, "Please enter the despriction of the ToDo. It can have a length up to 1024 characters: ", "^.{1,1024}$");
                    int importance = Integer.parseInt(getInputWithValidation(scan, "Please enter the importance of the ToDo. : ", "[0-1]"));
                    String categoryDescription = getInputWithValidation(scan, "Please enter the category of the ToDo. If you don't have one, type 'empty': ", generateDescriptionRegex(db.getCategories()));
                    int categoryID = db.findCategoryByDescription(categoryDescription);
                    db.createToDo(description, title, importance, categoryID, userID);
                    continueLoop = true;
                }

                // delete todo
                case 4 -> {
                    if(userID==0){
                        System.out.println("You are not logged in");
                        break;
                    } 
                    String title = getInputWithValidation(scan, "Please enter the title of the ToDo. It can have a length up to 128 characters: ", generateToDoRegex(db.getToDos(userID)));
                    db.deleteToDo(db.findToDoByTitle(title));
                    continueLoop = true;
                }

                // create category
                case 5 -> {
                    String categoryName = getInputWithValidation(scan, "Please enter the name of the category. It can have a length up to 32 characters: ", "^\\w{1,32}$");
                    db.createCategory(categoryName);
                    continueLoop = true;
                }

                // delete category
                case 6 -> {
                    String categoryDescription = getInputWithValidation(scan, "Please enter the category you want to delete: ", generateDescriptionRegex(db.getCategories()));
                    db.deleteCategory(categoryDescription);
                    continueLoop = true;
                }

                // delete user
                case 7 -> {
                    String username = getInputWithValidation(scan, "Please enter the username you want to delete: ", generateUserRegex(db.getUser()));
                    db.deleteUser(username);
                    continueLoop = true;
                }

                // end programm
                case 8 -> {
                    continueLoop = false;
                    System.out.println("The programm will end soon...");
                    continueLoop = true;
                }

                // log in 
                case 9 -> {
                    PasswordHasher ph = new PasswordHasher();
                    String username = getInputWithValidation(scan, "Please enter your username: ", "^.{1,32}$");
                    String password = ph.hashPassword(getInputWithValidation(scan, "Please enter your password: ", "^.{1,64}$"));
                    
                    if(checkLogin(password, username)){
                        userID = db.findUserByName(username);
                        loggedin = true;   
                        continueLoop = true; 
                        System.out.println("Log in was succesful...");
                    } else {
                        System.out.println("Your password or username is wrong. Log in was unsuccessful...");
                    }
                    
                }

                // create a new user
                case 10 -> {
                    PasswordHasher ph = new PasswordHasher();
                    String username = getInputWithValidation(scan, "Please enter your username. It can have a length up to 32 characters: ", "^.{1,32}$");
                    String password = getInputWithValidation(scan, "Please enter your password: It can have a length up to 64 characters: ", "^.{1,64}$");
                    String password2 = getInputWithValidation(scan, "Please reenter your password: ", "^"+password+"$");
                    db.addUser(username, ph.hashPassword(password2));     
                    System.out.println("The user '" + username + "' was successfully created.");     
                    continueLoop = true;         
                }
            }
        }
        scan.close();
    }

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

    private String getInputWithValidation(Scanner scanner, String question, String regex) {
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

    private String generateDescriptionRegex(String[] descriptions){
        String joinedDescriptions = String.join("|",descriptions);
        return "\\b(" + joinedDescriptions + ")\\b"; 
    }

    private String generateToDoRegex(String[] toDos){
        String joinedToDos = String.join("|",toDos);
        return "\\b(" + joinedToDos + ")\\b"; 
    }

    private String generateUserRegex(String[] users){
        String joinedUsers = String.join("|",users);
        return "\\b(" + joinedUsers + ")\\b"; 
    }


}
