package com.Connector;

import java.util.Scanner;

import com.cliNavigation;

public class DatabaseConnector {

    // connect with Database 
    private MariaDBConnector dbConn = new MariaDBConnector("wyrbill.de", 12089, "todo_app", "till", "123456");
    
    public DatabaseConnector(){

    }
  
    public void disconnect(){
        dbConn.close();
    }

    public String getErrorMessage(){
        return dbConn.getErrorMessage();
    }






    // <---------------- update methods ---------------->
    public void updateUserPassword(String password, int userID){
        dbConn.executeStatement("Update User set Password = '" + password + "' where ID = " + userID);
    }

    public void updateUserName(String name, int userID){
        dbConn.executeStatement("Update User set Name = '" + name + "' where ID = " + userID);
    }

    public void updateToDoTitle(String title, int todoID){
        dbConn.executeStatement("Update ToDo set Title = '" + title + "' where ID = " + todoID);
    }

    public void updateToDoDescription(String desc, int todoID){
        dbConn.executeStatement("Update ToDo set Description = '" + desc + "' where ID = " + todoID);
    }

    public void updateToDoPriority(int priority, int todoID){
        dbConn.executeStatement("Update ToDo set Priority = " + priority + " where ID = " + todoID);
    }

    public void updateToDoCategory(int categoryID, int todoID){
        dbConn.executeStatement("Update ToDo set CategoryID = " + categoryID + " where ID = " + todoID);
    }

    public void updateToDoOwner(int ownerID, int todoID){
        dbConn.executeStatement("Update ToDo set OwnerID = " + ownerID + " where ID = " + todoID);
    }

    public void updateCategory(String desc, int categoryID){
        dbConn.executeStatement("Update Category set Description = '" + desc + "' where ID = " + categoryID);
    }

    public void updateSessionID(int newUserID){
        int oldUserID = getSessionID();
        dbConn.executeStatement("update Session set userID = " + newUserID + " where userID = " + oldUserID);
    }










    // <---------------- add methods ---------------->
    public void addUser(String username, String password){
        if(findUserByName(username) != 0){
            return; // --> Username already exists
        }        
        dbConn.executeStatement("Insert into User (Name, Password) Values ('"+ username +"','"+ password +"')");
    }

    public void createCategory(String categoryName){
        dbConn.executeStatement("Insert into Category (Description) Values ('"+ categoryName +"')");
    }

    public void createToDo(String description, String title, int priority, int categoryID, int ownerID) {
    dbConn.executeStatement("Insert into ToDo (Description, Title, Date, Priority, CategoryID, OwnerID)" +
                            "Values ('" + description + "','" + title + "', NOW(), " + priority + "," + categoryID + "," + ownerID + ")");
    // get ID of last inserted item
    dbConn.executeStatement("SELECT LAST_INSERT_ID()");
    QueryResult qr = dbConn.getCurrentQueryResult();
    int newToDoID = Integer.parseInt(qr.getData()[0][0]);

    setSharedConnection(ownerID, newToDoID);
}


    public void setSharedConnection(int userID, int toDoID){
        if(findToDoByID(toDoID)==0) return;
        if(findUserByID(userID)==0) return;
        dbConn.executeStatement("Insert into Shared_ToDo_Users (user_id,todo_id)"+
                                "Values ("+userID+","+toDoID+")");
    }

    public void deleteSharedConnection(int userID, int toDoID){
        if(findToDoByID(toDoID)==0) return;
        if(findUserByID(userID)==0) return;
        dbConn.executeStatement("DELETE FROM Shared_ToDo_Users WHERE user_id = "+userID+" AND todo_id = "+toDoID);
    }










    // <---------------- delete methods ---------------->
    public boolean deleteUser(int userId) {
        if (userId == 1){
            System.out.println("Admin user cannot be deleted.");
            return false;
        } 
        // check if user has todos
        dbConn.executeStatement("SELECT ID FROM ToDo WHERE OwnerID = " + userId);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return false;

        int toDoCount = qr.getRowCount();
        
        // user has todos
        if (toDoCount > 0) {
            Scanner scanner = new Scanner(System.in);
            String response = cliNavigation.getInputWithValidation(scanner, 
                String.format("User with ID '%d' has %d ToDo(s). Do you want to delete them? (yes/no): ", userId, toDoCount), 
                "^(yes|no)$");
            
            if (response.equals("yes")) {
                // delete all ToDos of user
                for (int i = 0; i < toDoCount; i++) {
                    int toDoId = Integer.parseInt(qr.getData()[i][0]);
                    deleteToDo(toDoId);
                }
                System.out.printf("Deleted %d ToDo(s) linked to user with ID '%d'.%n", toDoCount, userId);
            } else {
                System.out.println("User deletion canceled.");
                return false;
            }
        }
        
        dbConn.executeStatement("DELETE FROM User WHERE ID = " + userId);
        System.out.printf("Deleted user with ID '%d'.%n", userId);
        return true;
    }

    public void deleteCategory(int categoryId) {
        if (categoryId == 1){
            System.out.println("You cannot delete the default category");
            return; 
        }
        // check if category is used 
        dbConn.executeStatement("SELECT ID FROM ToDo WHERE CategoryID = " + categoryId);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return;

        int toDoCount = qr.getRowCount();
        
        // category is used, delete corresponding todos if user wants to
        if (toDoCount > 0) {
            Scanner scanner = new Scanner(System.in);
            String response = cliNavigation.getInputWithValidation(scanner, 
                String.format("Category with ID '%d' is used by %d ToDo(s). Do you want to delete them? (yes/no): ", categoryId, toDoCount), 
                "^(yes|no)$");
    
            if (response.equals("yes")) {
                for (int i = 0; i < toDoCount; i++) {
                    int toDoId = Integer.parseInt(qr.getData()[i][0]);

                    // check if user is an admin
                    if(getSessionID() == 1){
                        deleteToDo(toDoId);
                    } else {
                        //check if users owns these ToDos
                        for(int j = 0; j < getToDosIDsByCategory(getSessionID(), categoryId).length; j++){
                            if(getToDosIDsByCategory(getSessionID(), categoryId)[j] == toDoId){
                                deleteToDo(toDoId);
                            } else {
                                System.out.println("You can only delete your own ToDos!");
                                System.out.println("Category deletion canceled.");
                                return;
                            }
                        }
                    }
                }
                System.out.printf("Deleted %d ToDo(s) linked to category with ID '%d'.%n", toDoCount, categoryId);
            } else {
                System.out.println("Category deletion canceled.");
                return;
            }
        }
        
        dbConn.executeStatement("DELETE FROM Category WHERE ID = " + categoryId);
        System.out.printf("Deleted category with ID '%d'.%n", categoryId);
    }

    public void deleteToDo(int toDoID) {
        if (findToDoByID(toDoID) == 0) return; // ToDo not found
        
        // find users with this todo 
        dbConn.executeStatement("SELECT user_id FROM Shared_ToDo_Users WHERE todo_id = " + toDoID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        int sharedCount = qr.getRowCount();
    
        for (int i = 0; i < sharedCount; i++) {
            int userId = Integer.parseInt(qr.getData()[i][0]);
            deleteSharedConnection(userId, toDoID); 
        }

        // delete
        dbConn.executeStatement("DELETE FROM ToDo WHERE ID = " + toDoID);
        System.out.printf("Deleted ToDo with ID '%d'.%n", toDoID);
    }
    






    

    // <---------------- getter methods ---------------->
    public String getNameOfUser(int id){
        dbConn.executeStatement("Select Name from User where ID = "+id);
        QueryResult qr = dbConn.getCurrentQueryResult();
        
        if(qr == null || qr.getRowCount() == 0) return "";

        return qr.getData()[0][0];
    }
    
    //returns ID of a user
    public int findUserByName(String username){
        dbConn.executeStatement("select ID from User where Name = '"+ username +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr == null || qr.getRowCount() == 0) return 0;

        return Integer.parseInt(qr.getData()[0][0]);
    }
    
    // returns ID of a user
    public int findUserByID(int ID){
        dbConn.executeStatement("select ID from User where ID = " + ID);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr == null || qr.getRowCount() == 0) return 0;

        return Integer.parseInt(qr.getData()[0][0]);
    }

    // return password of a user
    public String getPassword(int id){
        dbConn.executeStatement("select Password from User where id = "+ findUserByID(id));
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr == null || qr.getRowCount() == 0) return "";

        return qr.getData()[0][0];
    }

    public String[] getUser() {
        dbConn.executeStatement("select Name from User");
        QueryResult qr = dbConn.getCurrentQueryResult();
        
        if (qr == null || qr.getRowCount() == 0) return new String[0];
        
        int rowCount = qr.getRowCount();
        String[] result = new String[rowCount];
        
        for (int i = 0; i < rowCount; i++) {
            result[i] = qr.getData()[i][0];
        }
        return result;
    }
    

    public int[] getUserID(){
        dbConn.executeStatement("select ID from User");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new int[0];

        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];

        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result; 
    }

    // returns ID of a category
    public int findCategoryByDescription(String description){
        dbConn.executeStatement("select ID from Category where Description = '"+ description +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr == null || qr.getRowCount() == 0) return 0;

        return Integer.parseInt(qr.getData()[0][0]);
    }

    // returns ID of a category
    public int findCategoryByID(int ID){
        dbConn.executeStatement("select ID from Category where ID = '"+ ID +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        
        if(qr == null || qr.getRowCount() == 0) return 0;

        return Integer.parseInt(qr.getData()[0][0]);
    }

    // returns an array with all categories
    public String[] getCategories(){
        dbConn.executeStatement("select Description from Category");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new String[0];

        int rowCount = qr.getRowCount();
        String[] result = new String[rowCount];

        for(int i = 0; i < rowCount; i++){
            result[i] = qr.getData()[i][0];
        }
        return result; 
    }

    public int[] getCategoriesID(){
        dbConn.executeStatement("select ID from Category");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new int[0];

        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result; 
    }


    public int getSessionID(){
        dbConn.executeStatement("select userID from Session");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return 0;
        
        return Integer.parseInt(qr.getData()[0][0]);
    } 

    // returns ID of a todo
    public int findToDoByTitle(String title){
        dbConn.executeStatement("select ID from ToDo where title = '" + title + "'");
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return 0;

        return Integer.parseInt(qr.getData()[0][0]);
    }

    // returns ID of a todo
    public int findToDoByID(int toDoID){
        dbConn.executeStatement("select ID from ToDo where id = " + toDoID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        
        if(qr==null || qr.getRowCount() == 0) return 0;

        return Integer.parseInt(qr.getData()[0][0]);
    }

    // reteurns an array with all todos of transferred ownerID
    public String[] getToDos(int ownerID, String sort, String order){
        dbConn.executeStatement("select Title from ToDo where OwnerID = " + ownerID + " order by " + sort + " " + order);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new String[0];

        int rowCount = qr.getRowCount();
        String[] result = new String[rowCount];
        
        for(int i = 0; i < rowCount; i++){
            result[i] = qr.getData()[i][0];
        }
        return result;
    }

    public int[] getToDosID(int ownerID, String sort, String order){
        dbConn.executeStatement("select ID "+
                                "from ToDo "+
                                "where OwnerID = " + ownerID +
                                " order by " + sort + " " + order);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new int[0];

        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result;
    }

    public int[] getToDosIDsByCategory(int ownerID, int categoryID){
        dbConn.executeStatement("select ID "+
                                "from ToDo "+
                                "where OwnerID = " + ownerID +
                                " and CategoryID = " + categoryID);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new int[0];

        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result;
    }

    // returns an array with all todoIDs of the transferred ownerID
    public int[] getSharedConnections(int ownerID){
        dbConn.executeStatement("select todo_id from Shared_ToDo_Users where user_id = "+ownerID);
        QueryResult qr = dbConn.getCurrentQueryResult();

        if(qr==null || qr.getRowCount() == 0) return new int[0];
        
        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result;
    }
}