package com.pythonanywhere.brilliantcomputer;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author deepakkumar
 */
class MySQLDBFunctions {

  private final ArrayList<String> cmTables = new ArrayList();
  private final ArrayList<String> foTables = new ArrayList();
  
  public final ArrayList<String> symbol= new ArrayList();  
  public final ArrayList<String> open = new ArrayList();  
  public final ArrayList<String> high = new ArrayList();  
  public final ArrayList<String> low = new ArrayList();   
  public final ArrayList<String> close = new ArrayList();  
  public final ArrayList<String> pclose = new ArrayList();  
  public final ArrayList<String> vol = new ArrayList();
  
  public final ArrayList<String> record = new ArrayList();
    
  public void setTables(String database, String tnode)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak")) 
        {
            Statement statement = connection.createStatement();
            String sql = "SHOW TABLES";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String table = rs.getString("Tables_In_" + database);
                if(tnode.equals("cm")){
                    cmTables.add(table);
                }
                else if(tnode.equals("fo")){
                    foTables.add(table);
                }
           }
           connection.clearWarnings();
        }
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        System.err.println("SQLException");
    }
  }
  public ArrayList<String> getCmTbls()
  {
      //System.out.println(cmTables);
      return cmTables;
  }
  public ArrayList<String> getFoTbls()
  {
      //System.out.println(foTables);
      return foTables;
  }
  
  public void setRecord(String database, String tableName, String columns, String condition)
  {
    try 
    {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak")) {
            Statement statement = connection.createStatement();
            String sql = "SELECT " + columns + " FROM " + tableName + " " + condition;
            System.out.println(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String lsymbol = rs.getString("SYMBOL");
                String lopen = rs.getString("OPEN");
                String lhigh = rs.getString("HIGH");
                String llow = rs.getString("LOW");
                String lclose = rs.getString("CLOSE");
                String lpclose = rs.getString("PREVCLOSE");
                String lvol = rs.getString("TOTTRDQTY");
                symbol.add(lsymbol);
                open.add(lopen);
                high.add(lhigh);
                low.add(llow);
                close.add(lclose);
                pclose.add(lpclose);
                vol.add(lvol);
                
                
                record.add(lsymbol);
                record.add(lopen);
                record.add(lhigh);
                record.add(llow);
                record.add(lclose);
                record.add(lpclose);
                record.add(lvol);
                
                
            }
            connection.clearWarnings();
        }
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        ex.printStackTrace();
    }
  }
  public ArrayList<String> getRecord()
  {
      return record;
  }
    
}
