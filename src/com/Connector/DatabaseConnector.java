package com.Connector;

public class DatabaseConnector {
    private MariaDBConnector dbConn = new MariaDBConnector("wyrbill.de", 12089, "todo_app", "till", "123456");
    
    public DatabaseConnector(){
        if(dbConn==null){
            System.out.println("Connection could not be established...");   
        } else {
            System.out.println("Connection has been established...");
        }
    }


    // -------- CRUD User -------- 
    public void addUser(String username, String password){
        if(username.length()> 32){
            System.out.println("The chosen username is too long. It must be a maximum of 32 characters.");
            return;
        }
        dbConn.executeStatement("select Name from User where Name = '" + username + "'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() != 0){
            System.out.println("The chosen username is already taken. Please choose another!");
            return;
        }
        if(password.length()> 64){
            System.out.println("The chosen password is too long. It must be a maximum of 64 characters.");
            return;
        }
        
        dbConn.executeStatement("Insert into User (Name, Password) Values ('"+ username +"','"+ password +"')");
        System.out.println("The user with username: '" + username + "' was successfully created.");
    }

    public void deleteUser(String username){
        int id = findUserByName(username);
        if(id==0) return; // --> User not found
        dbConn.executeStatement("delete from User where id = " + id);
        System.out.println("The user with username: '" + username + "' was successfully deleted.");
    }

    //returns ID of a user
    public int findUserByName(String username){
        dbConn.executeStatement("select ID from User where Name = '"+ username +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        return Integer.parseInt(qr.getData()[0][0]);
    }
    
    public int findUserByID(int ID){
        dbConn.executeStatement("select ID from User where ID = " + ID);
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            System.out.println("The user with ID: '" + ID + "' was not found.");
            return 0;
        } 
        return Integer.parseInt(qr.getData()[0][0]);
    }

    public String getPassword(int id){
        String result = "";
        dbConn.executeStatement("select Password from User where id = "+ findUserByID(id));
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            return result;
        }
        return result = qr.getData()[0][0];
    }



    // -------- CRUD Category -------- 
    public void createCategory(String categoryName){
        if(categoryName.length() > 32){
            System.out.println("The chosen category name is too long. It must be a maximum of 32 characters.");
            return;
        }
        dbConn.executeStatement("Insert into Category (Description) Values ('"+ categoryName +"')");
        System.out.println("The category with name: '" + categoryName + "' was successfully created.");
    }

    public void deleteCategory(String description){
        int id = findCategoryByDescription(description);
        if(id==0) return; // --> Category not found
        dbConn.executeStatement("delete from Category where id = " + id);
        System.out.println("The category with name: '" + description + "' was successfully deleted.");
    }

    public int findCategoryByDescription(String description){
        dbConn.executeStatement("select ID from Category where Description = '"+ description +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        return Integer.parseInt(qr.getData()[0][0]);
    }

    public int findCategoryByID(int ID){
        dbConn.executeStatement("select ID from Category where ID = '"+ ID +"'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            System.out.println("The category with ID: '" + ID + "' was not found.");
            return 0;
        } 
        return Integer.parseInt(qr.getData()[0][0]);
    }


    // -------- CRUD ToDo -------- 
    public void createToDo(String description, String title, int important, int categoryID, int ownerID){
        if(description.length() > 1024){
            System.out.println("The chosen description for the ToDo is too long. It must be a maximum of 1024 characters.");
            return;
        }
        if(title.length() > 128){
            System.out.println("The chosen title for the ToDo is too long. It must be a maximum of 128 characters.");
            return;
        }
        if(important == 0 || important == 1){
            if(findCategoryByID(categoryID)==0){
                categoryID = 1;
            }
            if(findUserByID(ownerID)==0){
                return;
            }
            dbConn.executeStatement("Insert into ToDo (Description, Title, Date, Important, CategoryID, OwnerID)"+
                                    "Values ('" + description + "','" + title +"',NOW()," + important + "," + categoryID + "," + ownerID +")");
            setSharedConnection(ownerID, findToDoByTitle(title));
            System.out.println("The ToDo '" + title + "' was successfully created.");
        } else {
            System.out.println("The importance of a ToDo is indicated by 1 = important or 0 = not important.");
            return;
        }
    }

    public void deleteToDo(int id){
        if(findToDoByID(id) == 0) return; // ToDo not found
        dbConn.executeStatement("select OwnerID from ToDo where ID = " + id);
        QueryResult qr = dbConn.getCurrentQueryResult();
        int ownerID = Integer.parseInt(qr.getData()[0][0]);
        deleteSharedConnection(ownerID, id);
        dbConn.executeStatement("delete from ToDo where id = " + id);
        System.out.println("The ToDo with ID: '" + id + "' was successfully deleted.");
    }

    public int findToDoByTitle(String title){
        dbConn.executeStatement("select ID from ToDo where title = '" + title + "'");
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            System.out.println("The ToDo with title: '" + title + "' was not found.");
            return 0;
        }
        return Integer.parseInt(qr.getData()[0][0]);
    }

    public int findToDoByID(int id){
        dbConn.executeStatement("select ID from ToDo where id = " + id);
        QueryResult qr = dbConn.getCurrentQueryResult();
        if(qr.getRowCount() == 0){
            System.out.println("The ToDo with ID: '" + id + "' was not found.");
            return 0;
        }
        return Integer.parseInt(qr.getData()[0][0]);
    }

    public void printToDo(int id){
        
    }




    // -------- CRUD Shared_To_Do_Users -------- 
    public void setSharedConnection(int userID, int toDoID){
        if(findToDoByID(toDoID)==0) return;
        if(findUserByID(userID)==0) return;
        dbConn.executeStatement("Insert into Shared_ToDo_Users (user_id,todo_id)"+
                                "Values ("+userID+","+toDoID+")");
        System.out.println("The user with ID: '" + userID + "' has now access to the ToDo with ID: " + toDoID + "'");
    }

    public void deleteSharedConnection(int userID, int toDoID){
        if(findToDoByID(toDoID)==0) return;
        if(findUserByID(userID)==0) return;
        dbConn.executeStatement("DELETE FROM Shared_ToDo_Users WHERE user_id = "+userID+" AND todo_id = "+toDoID);
        System.out.println("The user with ID: '" + userID + "' has nolonger access to the ToDo with ID: " + toDoID + "'");
    }
}