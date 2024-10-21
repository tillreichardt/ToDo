package com.Connector;

import java.sql.*;

public class MariaDBConnector{
  private Connection connection;  
  private QueryResult currentQueryResult = null;
  private String message = null;

  public MariaDBConnector(String pIP, int pPort, String pDatabase, String pUsername, String pPassword){
    try {
        //Laden der Treiberklasse
        Class.forName("org.mariadb.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mariadb://"+pIP+":"+pPort+"/"+pDatabase, pUsername, pPassword);

    } catch (Exception e) {
        message = e.getMessage();
    }
  }	

  public void executeStatement(String pSQLStatement){  
    //Altes Ergebnis loeschen
    currentQueryResult = null;
    message = null;

    try {
      //Neues Statement erstellen
      Statement statement = connection.createStatement();

      //SQL Anweisung an die DB schicken.
        if (statement.execute(pSQLStatement)) { //Fall 1: Es gibt ein Ergebnis

        //Resultset auslesen
        ResultSet resultset = statement.getResultSet();

        //Spaltenanzahl ermitteln
        int columnCount = resultset.getMetaData().getColumnCount();
        
        //Spaltennamen und Spaltentypen in Felder uebertragen
        String[] resultColumnNames = new String[columnCount];
        String[] resultColumnTypes = new String[columnCount];
        for (int i = 0; i < columnCount; i++){
          resultColumnNames[i] = resultset.getMetaData().getColumnLabel(i+1);
          resultColumnTypes[i] = resultset.getMetaData().getColumnTypeName(i+1);
        }

        //Queue fuer die Zeilen der Ergebnistabelle erstellen
        Queue<String[]> rows = new Queue<String[]>();

        //Daten in Queue uebertragen und Zeilen zaehlen
        int rowCount = 0;
        while (resultset.next()){
          String[] resultrow =  new String[columnCount];
          for (int s = 0; s < columnCount; s++){
            resultrow[s] = resultset.getString(s+1);
          }
          rows.enqueue(resultrow);
          rowCount = rowCount + 1;
        }

        //Ergebnisfeld erstellen und Zeilen aus Queue uebertragen
        String[][] resultData = new String[rowCount][columnCount];
        int j = 0;
        while (!rows.isEmpty()){
          resultData[j] = rows.front();
          rows.dequeue();          
          j = j + 1;
        }
               
        //Statement schließen und Ergebnisobjekt erstellen
        statement.close();
        currentQueryResult =  new QueryResult(resultData, resultColumnNames, resultColumnTypes); 

      } else { //Fall 2: Es gibt kein Ergebnis.
        //Statement ohne Ergebnisobjekt schliessen
        statement.close();       
      }

    } catch (Exception e) {
      //Fehlermeldung speichern
      message = e.getMessage();
    }
  }

  public QueryResult getCurrentQueryResult(){
    return currentQueryResult;
  }

  public String getErrorMessage(){
    return message;
  }

  public void close(){
    try{
      connection.close();
    } catch (Exception e) {
      message = e.getMessage();
    }
  }
}