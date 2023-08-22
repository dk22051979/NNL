package com.pythonanywhere.brilliantcomputer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;

public class CSVInsert implements Runnable{

    private final String fileName;
    private final String fileSegment;

    public CSVInsert(String fileName, String fileSegment)        //4.1
    {

      this.fileName = fileName;
      this.fileSegment = fileSegment;
      
    }

   
    public void run()
    {
        if(fileSegment.equals("nsecm")) {
            insertCmCsv();
        } else if(fileSegment.equals("nsefo")) {
            insertFnoCsv();
        }

    }
    public void insertCmCsv(){
        
    //String fileName="fo28JUL2023bhav.csv";
        try
        {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/nsecm2023", "root","deepak");
            Statement statement = connection.createStatement();
            String tblName =new String(fileName.substring(0,11));
            
            String sql4 = "CREATE TABLE  IF NOT EXISTS " + tblName.toLowerCase() + " (  SYMBOL text DEFAULT NULL,   SERIES text DEFAULT NULL, "
                    + " OPEN float(19,2) DEFAULT NULL,  HIGH float(19,2) DEFAULT NULL,  LOW float(19,2) DEFAULT NULL,  CLOSE float(19,2) DEFAULT NULL,"
                    + " LAST float(19,2) DEFAULT NULL,  PREVCLOSE float(19,2) DEFAULT NULL,  TOTTRDQTY int(11) DEFAULT NULL,"
                    + " TOTTRDVAL float(19,2) DEFAULT NULL, TIMESTAMP text DEFAULT NULL, TOTALTRADES int(11) DEFAULT NULL,"
                    + " ISIN text DEFAULT NULL)";                  
            statement.executeUpdate(sql4);
            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                    
                    String sql3 = "INSERT INTO " + tblName.toLowerCase() + " (SYMBOL,SERIES,OPEN,HIGH,LOW,CLOSE,LAST,PREVCLOSE,TOTTRDQTY,TOTTRDVAL,TIMESTAMP,TOTALTRADES,ISIN) VALUES ('"  
                                  + columns[0].replaceAll("\\W","") +  "','"
                                  + columns[1].replaceAll("\\W","") +  "','"
                                  + columns[2] +  "','"
                                  + columns[3] +  "','"
                                  + columns[4] +  "','"
                                  + columns[5] +  "','"
                                  + columns[6] +  "','"
                                  + columns[7] +  "','"
                                  + columns[8] +  "','"
                                  + columns[9] +  "','"
                                  + columns[10] +  "','"
                                  + columns[11] +  "','"                                
                                  + columns[12].replaceAll("\\W","") +  "')";
                         statement.executeUpdate(sql3);
                         //System.out.println(sql3);    
                }
                ++count;
            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
    }
    public void insertFnoCsv(){
        
    //String fileName="fo28JUL2023bhav.csv";
        try
        {
            BufferedReader br = new BufferedReader( new FileReader(fileName));
            String strLine = "";
            String[] columns = null;
            int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/nsefo2023", "root","deepak");
            Statement statement = connection.createStatement();
            String tblName =new String(fileName.substring(0,11));
            String sql1 = "CREATE TABLE  IF NOT EXISTS " + tblName.toLowerCase() + " (  INSTRUMENT text DEFAULT NULL,   SYMBOL text DEFAULT NULL,  EXPIRY_DT text DEFAULT NULL, "
                    + " STRIKE_PR int(11) DEFAULT NULL,  OPTION_TYP text DEFAULT NULL,  OPEN float(19,2) DEFAULT NULL,  HIGH float(19,2) DEFAULT NULL,"
                    + " LOW float(19,2) DEFAULT NULL,  CLOSE float(19,2) DEFAULT NULL,  SETTLE_PR float(19,2) DEFAULT NULL,"
                    + " CONTRACTS int(11) DEFAULT NULL, VAL_INLAKH float(19,2) DEFAULT NULL, OPEN_INT int(11) DEFAULT NULL,"
                    + " CHG_IN_OI int(11) DEFAULT NULL, TIMESTAMP text DEFAULT NULL)";                  
            statement.executeUpdate(sql1);
            while( (strLine = br.readLine()) != null)
            {
                columns = strLine.split(",");
                if(count > 0)
                {
                    
                    String sql2 = "INSERT INTO " + tblName.toLowerCase() + " (INSTRUMENT,   SYMBOL ,  EXPIRY_DT , "
                    + " STRIKE_PR ,  OPTION_TYP ,  OPEN ,  HIGH ,"
                    + " LOW ,  CLOSE ,  SETTLE_PR,"
                    + " CONTRACTS , VAL_INLAKH, OPEN_INT,"
                    + " CHG_IN_OI, TIMESTAMP) VALUES ('"  + columns[0].replaceAll("\\W","") +  "','"
                                  + columns[1].replaceAll("\\W","") +  "','"
                                  + columns[2].replaceAll("\\W","") +  "','"
                                  + columns[3] +  "','"
                                  + columns[4] +  "','"
                                  + columns[5] +  "','"
                                  + columns[6] +  "','"
                                  + columns[7] +  "','"
                                  + columns[8] +  "','"
                                  + columns[9] +  "','"
                                  + columns[10] +  "','"
                                  + columns[11] +  "','"
                                  + columns[12] +  "','"
                                  + columns[13] +  "','"
                                  + columns[14].replaceAll("\\W","") +  "')";
                         statement.executeUpdate(sql2);
                         //System.out.println(sql2);    
                }
                ++count;
            }
         connection.close();
         br.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading csv file: " + e);
        }
    }
}
