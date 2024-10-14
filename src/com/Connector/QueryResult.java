package com.Connector;

public class QueryResult{
    private String[][] data;
    private String[] columnNames;
    private String[] columnTypes;
  
    QueryResult(String[][] pData, String[] pColumnNames, String[] pColumnTypes){
      data = pData;
      columnNames = pColumnNames;   
      columnTypes = pColumnTypes;
    }
  
    public String[][] getData(){
      return data;
    }
  
    public String[] getColumnNames(){
      return columnNames;
    }
  
    public String[] getColumnTypes(){
      return columnTypes;
    }
  
    public int getRowCount(){
      if (data != null )
        return data.length;
      else 
        return 0;
    }
  
    public int getColumnCount(){
      if (data != null && data.length > 0 && data[0] != null)
        return data[0].length;
      else
        return 0;
    }
  }