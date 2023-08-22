package com.pythonanywhere.brilliantcomputer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;

public class CandlestickChart {
    private static String[] CMTables  = null;
    private static String[] FOTables = null;
    private static String instType = null;
    private static String[] tables = null;    
    private static String cmdb = "nsecm2023";    
    private static String fodb = "nsefo2023";
    private static String scrip = null;
    
    private static int totalRecords = 0;
    private static int totalCMTables = 0;
    private static int totalFOTables = 0;

    
    private static Date[] date = null;;
    private static double[] high = null;;
    private static double[] low = null;;
    private static double[] open = null;;
    private static double[] close = null;;
    private static double[] volume = null;;
    
  public CandlestickChart(String instType, String[] tables, String scrip) {
      this.instType = instType;
      this.tables = tables;
      this.scrip = scrip;
      if(instType.equals("nsecm")) {
        CMTables = new String[365];
        this.getCMTables(cmdb);
        date = new Date[totalCMTables];
        high = new double[totalCMTables];
        low = new double[totalCMTables];
        open = new double[totalCMTables];
        close = new double[totalCMTables];
        volume = new double[totalCMTables];
      } else {
        FOTables = new String[365];
        this.getFOTables(fodb);        
        date = new Date[totalFOTables];
        high = new double[totalFOTables];
        low = new double[totalFOTables];
        open = new double[totalFOTables];
        close = new double[totalFOTables];
        volume = new double[totalFOTables];
      }
  }


    public static JPanel createDemoPanel() {
        JFreeChart chart;
        if(instType.equals("nsecm")) {
            chart = createChart(createCMDataset());
        } else {
            chart = createChart(createFODataset());
        }
        
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return (JPanel)panel;
    }   
    private static JFreeChart createChart(OHLCDataset dataset) {
        JFreeChart chart = ChartFactory.createCandlestickChart(scrip, "Time", "Value", dataset, true);

        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setDomainPannable(true);
        NumberAxis axis = (NumberAxis)plot.getRangeAxis();
        axis.setAutoRangeIncludesZero(false);
        axis.setUpperMargin(0.0D);
        axis.setLowerMargin(0.0D);
        return chart;
    }

    private static final Calendar calendar = Calendar.getInstance();
    private static Date createDate(int y, int m, int d, int hour, int min) {
        calendar.clear();
        calendar.set(y, m - 1, d, hour, min);
        return calendar.getTime();
    }


    private static OHLCDataset createCMDataset() {
/*
        date[0] = createDate(2023, 8, 1, 12, 0);
        getCMrec(cmdb, "cm01aug2023", scrip , 0);

        date[1] = createDate(2023, 8, 2, 12, 0);
        getCMrec(cmdb, "cm02aug2023", scrip , 1);
        
        date[2] = createDate(2023, 8, 3, 12, 0);
        getCMrec(cmdb, "cm03aug2023", scrip , 2);

        date[3] = createDate(2023, 8, 4, 12, 0);
        getCMrec(cmdb, "cm04aug2023", scrip , 3);
*/
       
        for(int i = 0; i <totalCMTables; ++i) {
            date[i] = createDate(2023, 8, (i+1), 12, 0);
            //System.out.println(CMTables[i]);
            getCMrec(cmdb, CMTables[i], scrip , i);
        }
        totalCMTables = 0;
        return (OHLCDataset)new DefaultHighLowDataset(scrip, date, high, low, open, close, volume);
    }


    private static void getCMrec(String database, String tb, String scrip, int idx)
    {
        try {
          Class.forName("com.mysql.jdbc.Driver");
          totalRecords = 0;
          Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
          Statement statement = connection.createStatement();
          String sql = "SELECT * FROM " + tb + " WHERE SERIES = 'EQ' AND SYMBOL='"+scrip+"'";
          //System.out.println(sql);
          ResultSet rs = statement.executeQuery(sql);
          while (rs.next()) {
              String o = rs.getString("OPEN");
              String h = rs.getString("HIGH");
              String l = rs.getString("LOW");
              String c = rs.getString("CLOSE");
              String v = rs.getString("TOTTRDQTY");
              open[idx] = Double.parseDouble(o);
              high[idx] = Double.parseDouble(h);
              low[idx] = Double.parseDouble(l);
              close[idx] = Double.parseDouble(c);
              volume[idx] = Double.parseDouble(v);
              //System.out.println(high[idx] + low[idx] + open[idx] + close[idx] + volume[idx]);

              ++totalRecords;
          }
          connection.clearWarnings();
          connection.close();
      }
      catch (ClassNotFoundException ex) {
          System.err.println("ClassNotFoundException");
      }
      catch (SQLException ex) {
          System.err.println("SQLException");

      }

    }  
    private static void getCMTables(String database)
    {
        try {
          Class.forName("com.mysql.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak")) {
                Statement statement = connection.createStatement();
                String sql = "SHOW TABLES";
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    String table = rs.getString("Tables_In_" + database);
                    CMTables[totalCMTables] = table;
                    ++totalCMTables;
                }
                connection.clearWarnings();
            }
      }
      catch (ClassNotFoundException ex) {
          System.err.println("ClassNotFoundException");
      }
      catch (SQLException ex) {
          //System.err.println("SQLException");
          ex.getMessage();
      }
    } 
    private static OHLCDataset createFODataset() {
/*

      date[0] = createDate(2023, 8, 1, 12, 0);
      getFOrec(fodb, "fo01aug2023", scrip , 0);

      date[1] = createDate(2023, 8, 2, 12, 0);
      getFOrec(fodb, "fo02aug2023", scrip , 1);

      

      date[2] = createDate(2023, 8, 3, 12, 0);
      getFOrec(fodb, "fo01aug2023", scrip , 2);

      date[3] = createDate(2023, 8, 4, 12, 0);
      getFOrec(fodb, "fo02aug2023", scrip , 3);
*/      
        for(int i = 0; i <totalFOTables; ++i) {
            date[i] = createDate(2023, 8, (i+1), 12, 0);
            //System.out.println(FOTables[i]);
            getFOrec(fodb, FOTables[i], scrip , i);
        }
      
      totalFOTables = 0;
      return (OHLCDataset)new DefaultHighLowDataset(scrip, date, high, low, open, close, volume);
    }

  private static void getFOrec(String database, String tb, String scrip, int idx)
  {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        totalRecords = 0;
        String[] str = scrip.split(" ", 5);
        //for (String a : str)
         //   System.out.println(a);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak");
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM " + tb + " WHERE INSTRUMENT='" + str[0] + "' AND SYMBOL='" + str[1] + "' AND EXPIRY_DT='" + str[2] + "' AND STRIKE_PR='" + str[3] + "' AND OPTION_TYP='" + str[4] + "'";
        //System.out.println(sql);
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            String i = rs.getString("INSTRUMENT");
            String s = rs.getString("SYMBOL");
            String ed = rs.getString("EXPIRY_DT");
            String sp = rs.getString("STRIKE_PR");
            String ot = rs.getString("OPTION_TYP");
            
            String o = rs.getString("OPEN");
            String h = rs.getString("HIGH");
            String l = rs.getString("LOW");
            String c = rs.getString("CLOSE");
            String v = rs.getString("OPEN_INT");            
            //String cio = rs.getString("CHG_IN_OI");
            
            open[idx] = Double.parseDouble(o);
            high[idx] = Double.parseDouble(h);
            low[idx] = Double.parseDouble(l);
            close[idx] = Double.parseDouble(c);
            volume[idx] = Double.parseDouble(v);            
            //Contracts[totalRecords] = new String(i + " " + s + " "+ ed + " " + sp + " " + ot);
            ++totalRecords;
        }
        connection.clearWarnings();
        connection.close();
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        //System.err.println("SQLException");
        ex.getMessage();
    }
  }
  private static void getFOTables(String database)
  {
    try {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, "root","deepak")) {
            Statement statement = connection.createStatement();
            String sql = "SHOW TABLES";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String table = rs.getString("Tables_In_" + database);
                FOTables[totalFOTables] = table;
                ++totalFOTables;
            }
            connection.clearWarnings();
        }
    }
    catch (ClassNotFoundException ex) {
        System.err.println("ClassNotFoundException");
    }
    catch (SQLException ex) {
        //System.err.println("SQLException");
        ex.getMessage();
    }
  }

}
