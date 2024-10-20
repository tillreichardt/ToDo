package com.Connector;

public class DatabaseConnector {
    private MariaDBConnector dbConn = new MariaDBConnector("wyrbill.de", 12089, "todo_app", "till", "123456");
    
    public DatabaseConnector(){

    }
  
    public void disconnect(){
        dbConn.close();
    }

    public String getErrorMessage(){
        return dbConn.getErrorMessage();
    }




    // -------- CRUD User -------- 
    public void addUser(String username, String password){
        if(findUserByName(username) != 0){
            return; // --> Username already exists
        }        
        dbConn.executeStatement("Insert into User (Name, Password) Values ('"+ username +"','"+ password +"')");
    }

    public void deleteUser(String username){
        int id = findUserByName(username);
        if(id==0) return; // --> User not found
        dbConn.executeStatement("delete from User where id = " + id);
    }

    //returns ID of a user
    public int findUserByName(String username){
        dbConn.executeStatement("select ID from User where Name = '"+ username +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr == null || qr.getRowCount() == 0){
            return 0;
        }
        return Integer.parseInt(qr.getData()[0][0]);
    }
    
    // returns ID of a user
    public int findUserByID(int ID){
        dbConn.executeStatement("select ID from User where ID = " + ID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            return 0;
        } 
        return Integer.parseInt(qr.getData()[0][0]);
    }

    // return password of a user
    public String getPassword(int id){
        String result = "";
        dbConn.executeStatement("select Password from User where id = "+ findUserByID(id));
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            return result;
        }
        return result = qr.getData()[0][0];
    }

    public String[] getUser(){
        dbConn.executeStatement("select Name from User");
        QueryResult qr = dbConn.getCurrentQueryResult();
        int rowCount = qr.getRowCount();
        String[] result = new String[rowCount];
        for(int i = 0; i < rowCount; i++){
            result[i] = qr.getData()[i][0];
        }
        return result; 
    }

    public int[] getUserID(){
        dbConn.executeStatement("select ID from User");
        QueryResult qr = dbConn.getCurrentQueryResult();
        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result; 
    }






    // -------- CRUD Category -------- 
    public void createCategory(String categoryName){
        dbConn.executeStatement("Insert into Category (Description) Values ('"+ categoryName +"')");
    }

    public void deleteCategory(String description){
        int id = findCategoryByDescription(description);
        if(id==0 || id == 1) return; // --> Category not found
        dbConn.executeStatement("delete from Category where id = " + id);
    }

    // returns ID of a category
    public int findCategoryByDescription(String description){
        dbConn.executeStatement("select ID from Category where Description = '"+ description +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        return Integer.parseInt(qr.getData()[0][0]);
    }

    // returns ID of a category
    public int findCategoryByID(int ID){
        dbConn.executeStatement("select ID from Category where ID = '"+ ID +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            System.out.println("The category with ID: '" + ID + "' was not found.");
            return 0;
        } 
        return Integer.parseInt(qr.getData()[0][0]);
    }

    // returns an array with all categories
    public String[] getCategories(){
        dbConn.executeStatement("select Description from Category");
        QueryResult qr = dbConn.getCurrentQueryResult();
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
        return Integer.parseInt(qr.getData()[0][0]);
    }

    public void updateSessionID(int newUserID){
        int oldUserID = getSessionID();
        dbConn.executeStatement("update Session set userID = " + newUserID + " where userID = " + oldUserID);
    }


    // -------- CRUD ToDo -------- 
    public void createToDo(String description, String title, int priority, int categoryID, int ownerID){
        dbConn.executeStatement("Insert into ToDo (Description, Title, Date, Priority, CategoryID, OwnerID)"+
        "Values ('" + description + "','" + title +"',NOW()," + priority + "," + categoryID + "," + ownerID +")");
        setSharedConnection(ownerID, findToDoByTitle(title));
    }

    public void deleteToDo(int toDoID){
        if(findToDoByID(toDoID) == 0) return; // ToDo not found
        dbConn.executeStatement("select OwnerID from ToDo where ID = " + toDoID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        int ownerID = Integer.parseInt(qr.getData()[0][0]);
        deleteSharedConnection(ownerID, toDoID);
        dbConn.executeStatement("delete from ToDo where id = " + toDoID);
    }

    // returns ID of a todo
    public int findToDoByTitle(String title){
        dbConn.executeStatement("select ID from ToDo where title = '" + title + "'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            return 0;
        }
        return Integer.parseInt(qr.getData()[0][0]);
    }

    // returns ID of a todo
    public int findToDoByID(int toDoID){
        dbConn.executeStatement("select ID from ToDo where id = " + toDoID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            return 0;
        }
        return Integer.parseInt(qr.getData()[0][0]);
    }

    // reteurns an array with all todos of transferred ownerID
    public String[] getToDos(int ownerID, String sort, String order){
                dbConn.executeStatement("select Title from ToDo where OwnerID = " + ownerID + " order by " + sort + " " + order);
        QueryResult qr = dbConn.getCurrentQueryResult();
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
        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result;
    }





    // -------- CRUD Shared_To_Do_Users -------- 
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

    // returns an array with all todoIDs of the transferred ownerID
    public int[] getSharedConnections(int ownerID){
        dbConn.executeStatement("select todo_id from Shared_ToDo_Users where user_id = "+ownerID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        int rowCount = qr.getRowCount();
        int[] result = new int[rowCount];
        for(int i = 0; i < rowCount; i++){
            result[i] = Integer.parseInt(qr.getData()[i][0]);
        }
        return result;
    }
}